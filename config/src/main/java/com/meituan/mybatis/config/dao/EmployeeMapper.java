package com.meituan.mybatis.config.dao;


import com.meituan.mybatis.config.bean.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper {

    public Employee getEmpById(Integer id);
}
