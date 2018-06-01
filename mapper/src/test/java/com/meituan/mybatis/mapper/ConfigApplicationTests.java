package com.meituan.mybatis.mapper;

import com.meituan.mybatis.mapper.bean.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigApplicationTests {

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
		Employee employee = sqlSession.selectOne("com.meituan.mybatis.config.bean.EmployeeMapper.selectEmp", 1);
		System.out.println(employee);

	}

}
