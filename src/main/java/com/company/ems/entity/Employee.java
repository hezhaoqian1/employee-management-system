package com.company.ems.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "员工信息")
public class Employee {

    @ApiModelProperty(value = "员工ID", example = "1", position = 1)
    private Long id;
    
    @ApiModelProperty(value = "员工姓名", example = "王小明", required = true, position = 2)
    private String name;
    
    @ApiModelProperty(value = "员工工号", example = "EMP202407191234", position = 3)
    private String employeeNo;
    
    @ApiModelProperty(value = "所属组织ID", example = "4", required = true, position = 4)
    private Long organizationId;
    
    @ApiModelProperty(value = "手机号码", example = "13888888888", position = 5)
    private String phone;
    
    @ApiModelProperty(value = "邮箱地址", example = "wangxiaoming@company.com", position = 6)
    private String email;
    
    @ApiModelProperty(value = "性别", example = "1", notes = "1-男，2-女", position = 7)
    private Integer gender;
    
    @ApiModelProperty(value = "职位", example = "Java开发工程师", position = 8)
    private String position;
    
    @ApiModelProperty(value = "部门", example = "技术部", position = 9)
    private String department;
    
    @ApiModelProperty(value = "入职日期", example = "2024-07-19", position = 10)
    private LocalDate hireDate;
    
    @ApiModelProperty(value = "生日", example = "1990-01-01", position = 11)
    private LocalDate birthday;
    
    @ApiModelProperty(value = "身份证号", example = "330106199001011234", position = 12)
    private String idCard;
    
    @ApiModelProperty(value = "家庭地址", example = "杭州市拱墅区xx路xx号", position = 13)
    private String address;
    
    @ApiModelProperty(value = "紧急联系人", example = "李小红", position = 14)
    private String emergencyContact;
    
    @ApiModelProperty(value = "紧急联系电话", example = "13999999999", position = 15)
    private String emergencyPhone;
    
    @ApiModelProperty(value = "薪资", example = "8000.00", position = 16)
    private BigDecimal salary;
    
    @ApiModelProperty(value = "员工状态", example = "1", notes = "1-在职，2-离职，3-试用期", position = 17)
    private Integer status;
    
    @ApiModelProperty(value = "创建时间", example = "2024-07-19T10:30:00", position = 18)
    private LocalDateTime createdTime;
    
    @ApiModelProperty(value = "更新时间", example = "2024-07-19T10:30:00", position = 19)
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "所属组织信息", position = 20)
    private Organization organization;
}
