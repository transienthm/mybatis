package com.meituan.mybatis.dynamic.dao;


import com.meituan.mybatis.dynamic.bean.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapperPlus {

    public Employee getEmpById(Integer id);

    public Employee getEmpAndDept(Integer id);
}
