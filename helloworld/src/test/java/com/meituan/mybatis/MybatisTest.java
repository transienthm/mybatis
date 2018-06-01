package com.meituan.mybatis;

import com.meituan.mybatis.bean.Employee;
import com.meituan.mybatis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: cab
 * \* Date: 2018/5/31
 * \* Time: 16:52
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class MybatisTest {

    /**
     *  1. 根据xml配置文件（全局配置文件）创建一个SqlSessionFactory对象
     *      有数据源一些运行环境信息    
     *  2.  sql映射文件，配置了每一个sql，以及sql的封装规则等
     *  3. 将sql映射文件注册在全局配置文件中
     *  4. 写代码
     *      1） 根据全局配置文件得到SqlSessionFactory
     *      2） 通过SqlSession工厂获取到SqlSession，使用SqlSession执行增删改查，一个SqlSession就是代表和数据库的一次会话，用完关闭
     *      3） 使用sql的唯一标识(id)来告诉mybatis执行哪个sql，sql全部保存在sql映射文件(mapper)中
     *      
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
}
