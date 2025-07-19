package com.company.ems.service.impl;

import com.company.ems.entity.Employee;
import com.company.ems.mapper.EmployeeMapper;
import com.company.ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeMapper.selectAll();
    }

    @Override
    public Employee getById(Long id) {
        return employeeMapper.selectById(id);
    }

    @Override
    public List<Employee> getByOrganizationId(Long organizationId) {
        return employeeMapper.selectByOrganizationId(organizationId);
    }

    @Override
    public Employee save(Employee employee) {
        // 自动生成工号
        if (StringUtils.isEmpty(employee.getEmployeeNo())) {
            employee.setEmployeeNo(generateEmployeeNo());
        } else {
            // 如果提供了工号，检查是否已存在
            Employee existingEmployee = employeeMapper.selectByEmployeeNo(employee.getEmployeeNo());
            if (existingEmployee != null) {
                throw new RuntimeException("工号已存在：" + employee.getEmployeeNo());
            }
        }

        // 设置默认状态
        if (employee.getStatus() == null) {
            employee.setStatus(1); // 默认在职
        }

        employeeMapper.insert(employee);
        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        // 检查员工是否存在
        Employee existingEmployee = employeeMapper.selectById(employee.getId());
        if (existingEmployee == null) {
            throw new RuntimeException("员工不存在");
        }

        // 如果没有提供工号，使用原有工号
        if (StringUtils.isEmpty(employee.getEmployeeNo())) {
            employee.setEmployeeNo(existingEmployee.getEmployeeNo());
        } else if (!existingEmployee.getEmployeeNo().equals(employee.getEmployeeNo())) {
            // 如果修改了工号，检查新工号是否重复
            Employee duplicateEmployee = employeeMapper.selectByEmployeeNo(employee.getEmployeeNo());
            if (duplicateEmployee != null && !duplicateEmployee.getId().equals(employee.getId())) {
                throw new RuntimeException("工号已存在：" + employee.getEmployeeNo());
            }
        }

        employeeMapper.updateById(employee);
        return employee;
    }

    @Override
    public void deleteById(Long id) {
        // 检查员工是否存在
        Employee employee = employeeMapper.selectById(id);
        if (employee == null) {
            throw new RuntimeException("员工不存在");
        }

        // 软删除：将状态改为离职
        employeeMapper.deleteById(id);
    }

    @Override
    public List<Employee> searchEmployees(String name, Long organizationId, Integer status) {
        return employeeMapper.selectByCondition(name, organizationId, status);
    }

    @Override
    public Employee getByEmployeeNo(String employeeNo) {
        return employeeMapper.selectByEmployeeNo(employeeNo);
    }
    
    private String generateEmployeeNo() {
        // 生成格式：EMP + 年月日 + 4位随机数
        String datePart = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = String.format("%04d", (int)(Math.random() * 10000));
        return "EMP" + datePart + randomPart;
    }
}
