package com.meituan.mybatis.mapper;

import com.meituan.mybatis.mapper.bean.Department;
import com.meituan.mybatis.mapper.bean.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * \* Created with IntelliJ IDEA.
 * \* User: cab
 * \* Date: 2018/6/5
 * \* Time: 12:36
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class DepartmentTest {

    private final static String PACKAGE = "com.meituan.mybatis.mapper.dao.DepartmentMapper.";
    @Test
    public void test01()  {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Department department = sqlSession.selectOne("com.meituan.mybatis.mapper.dao.DepartmentMapper.getDeptById", 1);
        System.out.println(department);
    }
    
    @Test
    public void test02() {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Department department = sqlSession.selectOne(PACKAGE + "getDeptByIdPlus", 1);
            System.out.println(department);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void test03() {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Department department = sqlSession.selectOne(PACKAGE + "getDeptByIdStep", 1);
            System.out.println(department.getDeptName());
            List<Employee> emps = department.getEmps(); 
            System.out.println(emps);
            System.out.println(emps.get(0).getDept());
        } finally {
            sqlSession.close();
        }
    }
    
    private SqlSessionFactory getSqlSessionFactory() {
        String resources = "mybatis-config.xml";
        InputStream is;
        SqlSessionFactory factory = null;
        try {
            is = Resources.getResourceAsStream(resources);
            factory = new SqlSessionFactoryBuilder().build(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return factory;
    }
}
