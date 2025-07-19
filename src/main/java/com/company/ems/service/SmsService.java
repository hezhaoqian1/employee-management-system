package com.company.ems.service;


import com.company.ems.dto.SmsBatchDTO;
import com.company.ems.entity.SmsRecord;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SmsService {

    /**
     * 批量发送短信（异步）
     */
    CompletableFuture<String> sendBatchSms(SmsBatchDTO
                                                   smsBatchDTO);

    /**
     * 单条短信发送（同步）
     *
     */
    boolean sendSingleSms(String phone, String content);

    /**
     * 根据批次ID查询发送记录
     */
    List<SmsRecord> getSmsRecordsByBatchId(String batchId);

    /**
     * 获取发送统计
     */
    String getSmsStatistics(String batchId);
}
