package com.company.ems.service;

import com.company.ems.entity.Employee;
import java.util.List;

public interface EmployeeService {

    /**
     * 获取所有员工（包含机构信息）
     */
    List<Employee> getAllEmployees();

    /**
     * 根据ID获取员工详情
     */
    Employee getById(Long id);

    /**
     * 根据机构ID获取员工列表
     */
    List<Employee> getByOrganizationId(Long
                                               organizationId);

    /**
     * 新增员工
     */
    Employee save(Employee employee);

    /**
     * 更新员工信息
     */
    Employee update(Employee employee);

    /**
     * 删除员工（软删除）
     */
    void deleteById(Long id);

    /**
     * 条件查询员工
     */
    List<Employee> searchEmployees(String name, Long
            organizationId, Integer status);

    /**
     * 根据员工工号查询（用于验证工号唯一性）
     */
    Employee getByEmployeeNo(String employeeNo);
}

