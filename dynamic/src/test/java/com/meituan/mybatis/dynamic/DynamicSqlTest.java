package com.meituan.mybatis.dynamic;

import com.meituan.mybatis.dynamic.bean.Department;
import com.meituan.mybatis.dynamic.bean.Employee;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: cab
 * \* Date: 2018/6/6
 * \* Time: 9:32
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class DynamicSqlTest {

    private final static String PACKAGE = "com.meituan.mybatis.dynamic.dao.DynamicMapper.";
    
    @Test
    public void test01() {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Employee employee = new Employee(4, "%e%", "", "3", null);
            List<Employee> emps = sqlSession.selectList(PACKAGE + "getEmpsByConditionIf", employee);
            for (Employee e : emps) {
                System.out.println(e);
            }
        } finally {
            sqlSession.close();            
        }
        
    }

    /**
     * test for choose
     */
    @Test
    public void test02() {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Employee employee = new Employee(null, "%e%", null, "3", null);
            List<Employee> emps = sqlSession.selectList(PACKAGE + "getEmpsByConditionChoose", employee);
            for (Employee e : emps) {
                System.out.println(e);
            }
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void test03() {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Department department = new Department(3, "营销部");
            Employee employee1 = new Employee(null, "harry", "harry@tom.com", "1", department);
            Employee employee2 = new Employee(null, "lily", "lily@tom.com", "1", department);

            List<Employee> emps = new ArrayList<>();
            emps.add(employee1);
            emps.add(employee2);
            sqlSession.insert(PACKAGE + "addEmpsByConditionForeach", emps);
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }

    private SqlSessionFactory getSqlSessionFactory() {
        String resources = "mybatis-config.xml";
        InputStream is = null;
        SqlSessionFactory sqlSessionFactory = null;
        try {
            is = Resources.getResourceAsStream(resources);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlSessionFactory;
    }
}
