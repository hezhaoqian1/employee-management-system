package com.company.ems.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
@ApiModel(description = "批量短信发送请求")
public class SmsBatchDTO {
    
    @ApiModelProperty(value = "员工ID列表", example = "[1, 2, 3, 4, 5]", required = true, position = 1)
    private List<Long> employeeIds;
    
    @ApiModelProperty(value = "短信内容", example = "【公司通知】欢迎新同事何兆乾加入技术部，大家相互认识一下！", required = true, position = 2)
    private String content;
    
    @ApiModelProperty(value = "批次ID", example = "WELCOME_BATCH_001", position = 3)
    private String batchId;
}

