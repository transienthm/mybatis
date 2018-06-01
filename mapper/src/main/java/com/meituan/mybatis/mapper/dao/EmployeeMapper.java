package com.meituan.mybatis.mapper.dao;


import com.meituan.mybatis.mapper.bean.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper {

    public Employee getEmpById(Integer id);

    public void addEmp(Employee employee);

    public void updateEmp(Employee employee);

    public void deleteEmpById(Integer id);
}
