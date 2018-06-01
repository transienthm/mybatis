package com.meituan.mybatis.mapper;

import com.meituan.mybatis.mapper.bean.Employee;
import com.meituan.mybatis.mapper.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;

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

    private SqlSessionFactory getSqlSessionFactory() throws Exception{
        String resources = "mybatis-config.xml";
        InputStream is = Resources.getResourceAsStream(resources);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        return sqlSessionFactory;
    }
}
