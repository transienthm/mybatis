# 1. HelloWorld

导入mybatis

```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.2</version>
</dependency>
```

## 1.1 配置文件法 

从XML中构建SqlSessionFactory

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://"/>
                <property name="username" value="root"/>
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="mapper/employee-mapper.xml"/>
    </mappers>
</configuration>
```

mapper

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--
 namespace: 名称空间
 id: 唯一标识
 returnType: 返回值类型
 #{id} 从传递过来的参数中取出id值
-->
<mapper namespace="com.meituan.mybatis.bean.EmployeeMapper">
    <select id="selectEmp" resultType="com.meituan.mybatis.bean.Employee">
        select * from employee where id = #{id}
    </select>
</mapper>
```

JavaBean

```java

public class Employee {
    private Integer id;
    private String lastName;
    private String email;
    private String gender;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}

```



单元测试

```java

    /**
     *  1. 根据xml配置文件（全局配置文件）创建一个SqlSessionFactory对象
     *      有数据源一些运行环境信息    
     *  2.  sql映射文件，配置了每一个sql，以及sql的封装规则等
     *  3. 将sql映射文件注册在全局配置文件中
     *  4. 写代码
     *      1） 根据全局配置文件得到SqlSessionFactory
     *      2） 通过SqlSession工厂获取到SqlSession，使用SqlSession执行增删改查，一个SqlSession就是代表和数据库的一次会话，用完关闭
     *      3） 使用sql的唯一标识(id)来告诉mybatis执行哪个sql，sql全部保存在sql映射文件(mapper)中
     * @throws Exception
     */
    @Test
    public void test() throws Exception{
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        //2. 获取SqlSession实例，能直接执行已经映射的sql语句
        SqlSession sqlSession = sqlSessionFactory.openSession();

        /**
         * Retrieve a single row mapped from the statement key and parameter.
         * @param <T> the returned object type
         * @param statement Unique identifier matching the statement to use. 传入唯一标识
         * @param parameter A parameter object to pass to the statement. 传入参数
         * @return Mapped object
         */
        Employee employee = sqlSession.selectOne("com.meituan.mybatis.bean.EmployeeMapper.selectEmp", 1);
        System.out.println(employee);

    }
```

**驼峰命名法问题：**

mybatis-config.xml配置文件配置时，**要注意节点顺序**

```xml
<properties>...</properties>
<settings>...</settings>
<typeAliases>...</typeAliases>
<typeHandlers>...</typeHandlers>
<objectFactory>...</objectFactory>
<objectWrapperFactory>...</objectWrapperFactory>
<plugins>...</plugins>
<environments>...</environments>
<databaseIdProvider>...</databaseIdProvider>
<mappers>...</mappers>
```

增加设置

```xml
支持驼峰命名法
<setting name="mapUnderscoreToCamelCase" value="true"/>
```



## 1.2 接口式编程

配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--
 namespace: 名称空间；接口式编程中，须指定为接口的全类名
 id: 唯一标识
 returnType: 返回值类型
 #{id} 从传递过来的参数中取出id值；接口式编程中，id改为方法名
-->
<mapper namespace="com.meituan.mybatis.dao.EmployeeMapper">
    <select id="getEmpById" resultType="com.meituan.mybatis.bean.Employee">
        select * from employee where id = #{id}
    </select>
</mapper>
```



mapper接口

```java
@Mapper
public interface EmployeeMapper {

    public Employee getEmpById(Integer id);
}
```

单元测试

```java
    @Test
    public void test01() throws Exception {
        //1. 获取SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        
        //2. 获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        
        try {
            //3. 获取接口的实现类对象
            // mybatis会为接口自动创建一个代理对象，代理对象去执行增删改查方法
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpById(1);
            System.out.println(employee);
        } finally {
            sqlSession.close();
        }

    }

    private SqlSessionFactory getSqlSessionFactory() throws Exception{
        String resources = "mybatis-config.xml";
        InputStream is = Resources.getResourceAsStream(resources);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        return sqlSessionFactory;
    }
```

## 1.3 总结

1. 接口式编程

   原生： Dao ====> DaoImpl

   mybatis：  Mapper ==== > xxMapper.xml

2. SqlSession代表和数据库的一次，用完必须关闭

3. SqlSession和connection一样，都是非线程安全的，每次使用都应该去获取新的对象，不能写为成员变量。

4. mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象

5. 两个重要的配置文件

   全局配置文件：mybatis-config.xml 包含数据库连接池信息，事务管理器信息等系统运行环境

   sql映射文件：保存了每一个sql语句的映射信息

# 2. Mybatis全局配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <!--
        1. mybatis可以使用properties来引入外部properties配置文件的内容
            属性：resource：引入类路径下的资源
                        url：引入网络路径或者磁盘路径下的资源
    -->
    <properties resource="dbconfig.properties">
    </properties>

    <!--
        2. 这是 MyBatis 中极为重要的调整设置，它们会改变 MyBatis 的运行时行为。
            setting：用来设置每一个设置项
                name：设置项名
                value：设置项取值
    -->
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
    
    <!--
        3.  typeAliases 类型别名是为 Java 类型设置一个短的名字。它只和 XML 配置有关，存在的意义仅在于用来减少类完全限定名的冗余。
            别名不区分大小写
            typeAlias：为某个java类型起别名
            type：指定要起别名的类型全类名；默认别名就是类名小写：employee
            alias：指定新的别名
            
            <package> ：为某个包下的所有类批量起别名
                    每一个在包 domain.blog 中的 Java Bean，在没有注解的情况下，会使用 Bean 的首字母小写的非限定类名来作为它的别名。 比如 domain.blog.Author 的别名为 author；若有注解，则别名为其注解值。
                    name：指定包名（为当前包以及下面所有的后代包的每一类都起一个包名）
                    
                    如果子包下有相同的类名，mybatis会起相同的包名从而报错。可以使用@Alias给特定类指定别名
    -->
    <typeAliases>
        <!--<typeAlias type="com.meituan.mybatis.config.bean.Employee" alias="emp"></typeAlias>-->
        <package name="com.meituan.mybatis.config"/>
    </typeAliases>
    
    <!--
        4. typeHandler  无论是 MyBatis 在预处理语句（PreparedStatement）中设置一个参数时，还是从结果集中取出一个值时， 都会用类型处理器将获取的值以合适的方式转换成 Java 类型。
    -->
    
    <!--
        5. plugins 插件
    -->
    
    <!--
        6. environment  
        MyBatis 可以配置成适应多种环境，这种机制有助于将 SQL 映射应用于多种数据库之中， 现实情况下有多种理由需要这么做。
        default指定使用某种环境来切换环境，可以达到快速切换环境。
        environment配置一个具体的环境信息，id代表当前环境的唯一标识，必须有两个标签：
            transactionManager：事务管理器，
                    type指定事务管理器的类型 在 MyBatis 中有两种类型的事务管理器（也就是 type=”[JDBC|MANAGED]”）：
                        JDBC：typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
                        MANAGED：typeAliasRegistry.registerAlias("MANAGED", ManagedTransactionFactory.class);
                        支持自定义事务管理器，实现TransactionFactory接口即可
            dataSource：数据源
                type：数据源类型，有三种内建的数据源类型（也就是 type=”[UNPOOLED|POOLED|JNDI]”）：
                支持自定义数据源，实现DataSourceFactory接口，type为自定义数据源的全类名
    -->
    
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>
    
    <!--
        7. databaseIdProvider:  支持多数据库厂商
    -->
    
    <!--
        8. mappers映射器
            mapper注册一个sql映射
                resource：引用类路径下的sql映射文件
                url：引用网络路径下或者磁盘路径下的映射文件
                class：使用映射器接口实现类的完全限定类名
                    1. 有sql映射文件，映射文件名必须和接口同名，并且放在与接口同一目录下
                    2. 没有sql映射文件，所有的sql都是利用注解写在接口上
                package：将包内的映射器接口实现全部注册为映射器
    -->
    <mappers>
        <mapper resource="mapper/employee-mapper.xml"/>
    </mappers>
    
    <!--
        标签的编写是有顺序的
    -->
</configuration>
```



# 3.Mybatis映射文件 

MyBatis 的真正强大在于它的映射语句，也是它的魔力所在。由于它的异常强大，映射器的 XML 文件就显得相对简单。如果拿它跟具有相同功能的 JDBC 代码进行对比，你会立即发现省掉了将近 95% 的代码。MyBatis 就是针对 SQL 构建的，并且比普通的方法做的更好。 

## 3.1 获取自增主键

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--
 namespace: 名称空间
 id: 唯一标识
 returnType: 返回值类型
 #{id} 从传递过来的参数中取出id值
-->
<mapper namespace="com.meituan.mybatis.mapper.dao.EmployeeMapper">
    <select id="getEmpById" resultType="com.meituan.mybatis.mapper.bean.Employee">
        SELECT *
        FROM employee
        WHERE id = #{id}
    </select>

    <!--parameterType：可以省略
        mysql支持自增主键，自增主键的获取，mybatis也是利用statement.getGeneratedKeys(),
        useGeneratedKeys="true"：使用自增主键获取主键值策略
        keyProperty：指定对应的主键属性，也就是mybatis获取到主键值以后，将这个值封装给JavaBean的哪个属性
    -->
    <insert id="addEmp" parameterType="com.meituan.mybatis.mapper.bean.Employee"
        useGeneratedKeys="true" keyProperty="id"
    >
        INSERT INTO employee (last_name, email, gender)
        VALUES (#{lastName}, #{email}, #{gender})
    </insert>

    <update id="updateEmp">
        UPDATE employee
        SET last_name = #{lastName}, email = #{email}, gender = #{gender}
        WHERE id = #{id}
    </update>

    <delete id="deleteEmpById">
        DELETE FROM employee WHERE id=#{id}
    </delete>
</mapper>
```

单元测试

```java
 /**
     * 1. mybatis允许增删改直接定义以下类型返回值
     *      Integer Long Boolean
     *  2. 手动提交数据
     * @throws Exception
     */
    @Test
    public void test02() throws Exception {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //1. 获取到的SqlSession不会自动提交
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = new Employee(null, "jerry", "jerry@tom.com", "2");
            System.out.println(employee);
            System.out.println("============");
            mapper.addEmp(employee);
            System.out.println(employee);
            //            employee.setLastName("jason");
//            employee.setId(3);
//            mapper.updateEmp(employee);
//            mapper.deleteEmpById(3);
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }

    private SqlSessionFactory getSqlSessionFactory() throws Exception{
        String resources = "mybatis-config.xml";
        InputStream is = Resources.getResourceAsStream(resources);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        return sqlSessionFactory;
    }
```



## 3.2 参数处理 

1）单个参数：mybatis不会做特殊处理

2）多个参数

​	异常：

```
org.apache.ibatis.exceptions.PersistenceException: 
### Error querying database.  Cause: org.apache.ibatis.binding.BindingException: Parameter 'id' not found. Available parameters are [arg1, arg0, param1, param2]
### Cause: org.apache.ibatis.binding.BindingException: Parameter 'id' not found. Available parameters are [arg1, arg0, param1, param2]
```

​	操作：

​		方法：public Employee getEmpByIdAndLastName(Integer id ,String lastName);

​		取值：#{id},#{lastName}



​	mybatis会特殊处理，多个参数会被封装成一个map

​	key：param1....paramN

​	value：传入的参数值

​	\#{}就是从map中获取指定的key值，或者参数的索引也可以

命名参数：

​	明确指定封装参数值map的key: @Param("id")

POJO:

​	如果多个参数正好是业务逻辑的数据模型，可以直接传入POJO：

​	\#{属性名}：取出传入的pojo的属性值

Map：

如果多个参数不是业务模型中的数据，没有对应的pojo，不经常使用，为了方便，也可以传入map

如果多个参数不是业务模型中的数据，但是经常使用，推荐写一个TO(Transfer Object) 数据传输对象



3）参数封装扩展思考：

1. public Employee getEmp(@Param("id"))Integer id, String lastName);

   取值：id==》#{id/param1} lastName===>#{param2}

2. public Employee getEmp(Integer id, @Param("e") Employee emp);

   取值：id===》#{param1} lastName===》#{param2.LastName/e.lastName}

3. 特别注意：如果是Collection（List、Set）类型或者数组

   也会特殊处理，也是把传入的list或者数组封装在map中

   ​	key：Collection(collection)，如果是List还可以使用（list）

   ​	数组（array）

   public Employee getEmpById(List<Integer> ids);

   取值：取出第一个id的值：#{list[0]}

## 3.3 结合源码，mybatis如何处理参数

ParamNameResolver解析参数封装map

(1) names:(0=id, 1=lastName)

​	1) 获取每个标注Param注解的参数param值：id，lastName，赋值给name

​	2）每次解析一个参数给map中保存信息:key是索引值， value是name的值

​		name的值：

​			标注了param注解，注解的值

​			没有标注：

​				1、全局配置：useActualParamName，name=参数名（要求JDK1.8）

​				2、name=map.size() 相当于当前元素的索引

​	names:{0=id, 1=lastName}	



```java
  public Object getNamedParams(Object[] args) {
    final int paramCount = names.size();
      //1. 参数为null直接返回
    if (args == null || paramCount == 0) {
      return null;
      //2. 如果只有一个元素并且没有param注解：args[0]，单个参数直接返回
    } else if (!hasParamAnnotation && paramCount == 1) {
      return args[names.firstKey()];
      //3. 多个元素或者有Param标注
    } else {
      final Map<String, Object> param = new ParamMap<Object>();
      int i = 0;
      // 4. 遍历names，构造器的时候就已经确定
      for (Map.Entry<Integer, String> entry : names.entrySet()) {
        //names的value作为新map的key，nameskey作为取值的参考
        //eg：{id=args[0], lastName=args[1]}，因此可以在映射文件中取到相应的值
        param.put(entry.getValue(), args[entry.getKey()]);
        // add generic param names (param1, param2, ...)
        final String genericParamName = GENERIC_NAME_PREFIX + String.valueOf(i + 1);
        // ensure not to overwrite parameter named with @Param
        if (!names.containsValue(genericParamName)) {
          param.put(genericParamName, args[entry.getKey()]);
        }
        i++;
      }
      return param;
    }
  }
```

## 3.4 参数值的获取

\#{}：可以获取map中的值或者pojo对象属性的值

${}：可以获取map中的值或者pojo对象属性的值

区别：\#{}是以预编译的形式，将参数设置到sql语句中，PreparedStatement

​	   ${}：取出的值直接拼装在sql语句中，会有安全问题

​	大多情况下，取参数的值都应该使用#{}，在某些情况下，原生jdbc不支持占位符的地方可以使用${}进行取值，

比如分表；按年份分表拆分 select * from 2017_salary可以写为 select * from ${year}_salary



## 3.5 #{}取值规则

更丰富的用法

规定参数的一些规则：

​	javaType、jdbcType、mode（存储过程）、numericScale、resultMap、typeHandler、jdbcTypeName、expression

jdbcType参演需要在某种特定的条件下被设置

​	在数据为null的时候，有些数据库可能无法识别mybatis对null的默认处理，如oracle，mybatis对所有的null都映射为原生Jdbc的OTHER类型， oracle无法处理，mysql可以处理

1、#{email, jdbcType=OTHER}

2、全局配置文件mybatis-config.xml中：`<setting name="jdbcTypeForNull" value="NULL" />`



## 3.6 Select返回List、Map

- 返回List

```xml
    <!--resultType：如果返回的是一个集合，要写集合中元素的类型-->
    <select id="getEmpsByLastNameLike" resultType="com.meituan.mybatis.mapper.bean.Employee">
        SELECT * FROM employee WHERE last_name LIKE #{lastName}
    </select>
```

- 返回Map，key就是列名，值是对应的值

``` xml
<!--map是mybatis自定义的别名-->
<select id="getEmpByIdReturnMap" resultType="map">
    SELECT * FROM employee WHERE id=#{id}
</select>
```

- 多条纪录封装成一个map，Map<Integer, Employee> 键是这条纪录的主键，值是记录封装的JavaBean

```xml
    <select id="getEmpByLastNameLikeReturnMap" resultType="com.meituan.mybatis.mapper.bean.Employee">
        SELECT * FROM employee WHERE last_name LIKE #{lastName}
    </select>
    
```

```java
@MapKey("id")
public Map<Integer, Employee> getEmpByLastNameLikeReturnMap(String lastName);
```

