package com.meituan.mybatis.dynamic.dao;


import com.meituan.mybatis.dynamic.bean.Employee;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface EmployeeMapper {

    public Employee getEmpById(Integer id);

    public Employee getEmpByIdAndLastName(@Param("id") Integer id, @Param("lastName") String lastName);

    public void addEmp(Employee employee);

    public void updateEmp(Employee employee);

    public void deleteEmpById(Integer id);

    @MapKey("id")
    public Map<Integer, Employee> getEmpByLastNameLikeReturnMap(String lastName);
}
