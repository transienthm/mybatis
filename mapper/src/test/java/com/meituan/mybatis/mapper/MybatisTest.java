package com.meituan.mybatis.mapper;

import com.meituan.mybatis.mapper.bean.Employee;
import com.meituan.mybatis.mapper.dao.EmployeeMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.mockito.Mock;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: cab
 * \* Date: 2018/6/1
 * \* Time: 11:15
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class MybatisTest {
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

    @Test
    public void test03() throws Exception{
        SqlSession sqlSession = getSqlSessionFactory().openSession();
        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
           // Employee employee = new Employee(1, "tom", "jerry@tom.com", "2");
            
            Employee tom = mapper.getEmpByIdAndLastName(1,"tom");
            System.out.println(tom);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void test04() throws Exception {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Employee> emps = sqlSession.selectList("getEmpsByLastNameLike","%jer%");
        for (Employee employee : emps) {
            System.out.println("=============");
            System.out.println(employee);
            System.out.println("=============");
        }

        Map<String, Object> getEmpByIdReturnMap = sqlSession.selectOne("getEmpByIdReturnMap", 1);
        System.out.println(getEmpByIdReturnMap);
    }

    @Test
    public void test05() throws Exception {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Map<Integer, Employee> map = mapper.getEmpByLastNameLikeReturnMap("%jer%");
        System.out.println(map);
    }

    private SqlSessionFactory getSqlSessionFactory() throws Exception{
        String resources = "mybatis-config.xml";
        InputStream is = Resources.getResourceAsStream(resources);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        return sqlSessionFactory;
    }
}
