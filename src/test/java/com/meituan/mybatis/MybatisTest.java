package com.meituan.mybatis;

import com.meituan.mybatis.bean.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

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
     *  2. 
     * @throws Exception
     */
    @Test
    public void test() throws Exception{
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        //2. 获取SqlSession实例，能直接执行已经映射的sql语句
        SqlSession sqlSession = sqlSessionFactory.openSession();

        Employee employee = sqlSession.selectOne("com.meituan.mybatis.bean.EmployeeMapper.selectEmp", 1);
        System.out.println(employee);

    }
}
