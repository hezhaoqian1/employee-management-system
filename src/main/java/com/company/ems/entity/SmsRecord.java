package com.company.ems.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "短信发送记录")
public class SmsRecord {

    @ApiModelProperty(value = "记录ID", example = "1", position = 1)
    private Long id;
    
    @ApiModelProperty(value = "批次ID", example = "WELCOME_BATCH_001", position = 2)
    private String batchId;
    
    @ApiModelProperty(value = "员工ID", example = "1", position = 3)
    private Long employeeId;
    
    @ApiModelProperty(value = "手机号码", example = "13888888888", position = 4)
    private String phone;
    
    @ApiModelProperty(value = "短信内容", example = "【公司通知】欢迎新同事加入我们的团队！", position = 5)
    private String content;
    
    @ApiModelProperty(value = "发送状态", example = "1", notes = "0-待发送，1-发送成功，2-发送失败", position = 6)
    private Integer status;
    
    @ApiModelProperty(value = "发送时间", example = "2024-07-19T10:30:00", position = 7)
    private LocalDateTime sendTime;
    
    @ApiModelProperty(value = "错误信息", example = "发送失败原因", position = 8)
    private String errorMessage;
    
    @ApiModelProperty(value = "创建时间", example = "2024-07-19T10:30:00", position = 9)
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "员工信息", position = 10)
    private Employee employee;
}
