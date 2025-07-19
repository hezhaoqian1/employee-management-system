package com.company.ems.controller;

import com.company.ems.common.ApiResponse;
import com.company.ems.entity.Employee;
import com.company.ems.exception.BusinessException;
import com.company.ems.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "2. 员工管理")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @ApiOperation(value = "获取所有员工", notes = "获取所有员工列表")
    @GetMapping
    public ApiResponse<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ApiResponse.success("获取员工列表成功", employees);
    }

    @ApiOperation(value = "获取员工详情", notes = "根据ID获取员工详细信息")
    @GetMapping("/{id}")
    public ApiResponse<Employee> getEmployeeById(@ApiParam(value = "员工ID", required = true) @PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException("INVALID_PARAMETER", "员工ID不能为空且必须大于0");
        }
        Employee employee = employeeService.getById(id);
        if (employee == null) {
            throw new BusinessException("EMPLOYEE_NOT_FOUND", "员工信息不存在");
        }
        return ApiResponse.success("获取员工详情成功", employee);
    }

    @ApiOperation(value = "根据组织获取员工", notes = "根据组织ID获取员工列表")
    @GetMapping("/organization/{orgId}")
    public ApiResponse<List<Employee>> getEmployeesByOrganization(@ApiParam(value = "组织ID", required = true) @PathVariable Long orgId) {
        if (orgId == null || orgId <= 0) {
            throw new BusinessException("INVALID_PARAMETER", "组织ID不能为空且必须大于0");
        }
        List<Employee> employees = employeeService.getByOrganizationId(orgId);
        return ApiResponse.success("获取组织员工列表成功", employees);
    }

    @ApiOperation(value = "搜索员工", notes = "根据条件搜索员工")
    @GetMapping("/search")
    public ApiResponse<List<Employee>> searchEmployees(
            @ApiParam(value = "员工姓名") @RequestParam(required = false) String name,
            @ApiParam(value = "组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam(value = "状态", defaultValue = "1") @RequestParam(required = false, defaultValue = "1") Integer status) {
        List<Employee> employees = employeeService.searchEmployees(name, organizationId, status);
        return ApiResponse.success("搜索员工成功", employees);
    }

    @ApiOperation(value = "根据工号获取员工", notes = "根据员工工号获取员工信息")
    @GetMapping("/by-employee-no/{employeeNo}")
    public ApiResponse<Employee> getEmployeeByNo(@ApiParam(value = "员工工号", required = true) @PathVariable String employeeNo) {
        if (employeeNo == null || employeeNo.trim().isEmpty()) {
            throw new BusinessException("INVALID_PARAMETER", "员工工号不能为空");
        }
        Employee employee = employeeService.getByEmployeeNo(employeeNo);
        if (employee == null) {
            throw new BusinessException("EMPLOYEE_NOT_FOUND", "员工信息不存在");
        }
        return ApiResponse.success("获取员工信息成功", employee);
    }

    @ApiOperation(value = "创建员工", notes = "新增员工信息")
    @PostMapping
    public ApiResponse<Employee> createEmployee(@RequestBody Employee employee) {
        if (employee == null) {
            throw new BusinessException("INVALID_PARAMETER", "员工信息不能为空");
        }
        if (employee.getName() == null || employee.getName().trim().isEmpty()) {
            throw new BusinessException("INVALID_PARAMETER", "员工姓名不能为空");
        }
        if (employee.getOrganizationId() == null) {
            throw new BusinessException("INVALID_PARAMETER", "所属组织不能为空");
        }
        Employee savedEmployee = employeeService.save(employee);
        return ApiResponse.success("创建员工成功", savedEmployee);
    }

    @ApiOperation(value = "更新员工", notes = "修改员工信息")
    @PutMapping("/{id}")
    public ApiResponse<Employee> updateEmployee(@ApiParam(value = "员工ID", required = true) @PathVariable Long id, @RequestBody Employee employee) {
        if (id == null || id <= 0) {
            throw new BusinessException("INVALID_PARAMETER", "员工ID不能为空且必须大于0");
        }
        if (employee == null) {
            throw new BusinessException("INVALID_PARAMETER", "员工信息不能为空");
        }
        employee.setId(id);
        Employee updatedEmployee = employeeService.update(employee);
        return ApiResponse.success("更新员工成功", updatedEmployee);
    }

    @ApiOperation(value = "删除员工", notes = "删除员工信息")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteEmployee(@ApiParam(value = "员工ID", required = true) @PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException("INVALID_PARAMETER", "员工ID不能为空且必须大于0");
        }
        Employee employee = employeeService.getById(id);
        if (employee == null) {
            throw new BusinessException("EMPLOYEE_NOT_FOUND", "员工信息不存在");
        }
        employeeService.deleteById(id);
        return ApiResponse.success("删除员工成功", null);
    }
}