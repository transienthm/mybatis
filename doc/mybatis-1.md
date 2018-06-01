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