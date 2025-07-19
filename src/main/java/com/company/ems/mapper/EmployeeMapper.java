package com.company.ems.mapper;

import com.company.ems.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    List<Employee> selectAll();

    List<Employee> selectByOrganizationId(@Param("organizationId") Long organizationId);

    Employee selectById(@Param("id") Long id);

    Employee selectByEmployeeNo(@Param("employeeNo") String employeeNo);

    List<Employee> selectByIds(@Param("ids") List<Long> ids);

    int insert(Employee employee);

    int updateById(Employee employee);

    int deleteById(@Param("id") Long id);

    List<Employee> selectByCondition(@Param("name") String name, @Param("organizationId") Long organizationId, @Param("status") Integer status);
}

