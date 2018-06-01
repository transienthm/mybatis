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

