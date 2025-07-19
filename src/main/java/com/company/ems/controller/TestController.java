package com.company.ems.controller;

import com.company.ems.dto.SmsBatchDTO;
import com.company.ems.entity.Employee;
import com.company.ems.entity.Organization;
import com.company.ems.entity.SmsRecord;
import com.company.ems.service.EmployeeService;
import com.company.ems.service.OrganizationService;
import com.company.ems.service.SmsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Api(tags = "4. 测试接口")
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/org-tree")
    public List<Organization> testOrganizationTree() {
        return organizationService.getOrganizationTree();
    }

    @GetMapping("/org-by-id")
    public Organization testGetById() {
        return organizationService.getById(1L);
    }

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public List<Employee> testGetAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/employee-by-id")
    public Employee testGetEmployeeById() {
        return employeeService.getById(1L);
    }

    @GetMapping("/employees-by-org")
    public List<Employee> testGetEmployeesByOrg() {
        return employeeService.getByOrganizationId(4L);
    }

    @GetMapping("/search-employees")
    public List<Employee> testSearchEmployees() {
        return employeeService.searchEmployees("张", null, 1);
    }


    @Autowired
    private SmsService smsService;

    @PostMapping("/send-sms")
    public String testSendBatchSms() {
        SmsBatchDTO dto = new SmsBatchDTO();
        dto.setEmployeeIds(Arrays.asList(1L, 2L, 3L, 4L, 5L));
        dto.setContent("【公司通知】请各位员工按时参加明天上午9点的全员大会，请勿迟到。");
        dto.setBatchId("BATCH_" + System.currentTimeMillis());

        smsService.sendBatchSms(dto);
        return "短信发送任务已提交：" + dto.getBatchId() + "，请稍后查看发送结果";
    }

    @GetMapping("/sms-records/{batchId}")
    public List<SmsRecord> getSmsRecords(@PathVariable String batchId) {
        return smsService.getSmsRecordsByBatchId(batchId);
    }

    @GetMapping("/sms-statistics/{batchId}")
    public String getSmsStatistics(@PathVariable String batchId) {
        return smsService.getSmsStatistics(batchId);
    }

    @GetMapping("/send-sms-get")
    public String testSendBatchSmsGet() {
        return testSendBatchSms();
    }

}
