package com.meituan.mybatis.mapper.dao;


import com.meituan.mybatis.mapper.bean.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapperPlus {

    public Employee getEmpById(Integer id);

    public Employee getEmpAndDept(Integer id);
}
