package com.company.ems.controller;

import com.company.ems.dto.SmsBatchDTO;
import com.company.ems.entity.SmsRecord;
import com.company.ems.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Api(tags = "3. 短信管理")
@RestController
@RequestMapping("/api/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    /**
     * 批量发送短信
     */
    @PostMapping("/batch")
    public ResponseEntity<String> sendBatchSms(@RequestBody SmsBatchDTO smsBatchDTO) {
        try {
            // 验证请求参数
            if (smsBatchDTO.getEmployeeIds() == null || smsBatchDTO.getEmployeeIds().isEmpty()) {
                return ResponseEntity.badRequest().body("员工ID列表不能为空");
            }
            if (smsBatchDTO.getContent() == null || smsBatchDTO.getContent().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("短信内容不能为空");
            }

            // 生成批次ID（如果没有提供）
            if (smsBatchDTO.getBatchId() == null || smsBatchDTO.getBatchId().trim().isEmpty()) {
                smsBatchDTO.setBatchId("BATCH_" + System.currentTimeMillis());
            }

            // 异步发送短信
            CompletableFuture<String> result = smsService.sendBatchSms(smsBatchDTO);
            
            return ResponseEntity.ok("短信发送任务已提交，批次ID：" + smsBatchDTO.getBatchId() + 
                                   "，发送给 " + smsBatchDTO.getEmployeeIds().size() + " 名员工");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("发送失败：" + e.getMessage());
        }
    }

    /**
     * 根据批次ID获取短信记录
     */
    @GetMapping("/records/{batchId}")
    public ResponseEntity<List<SmsRecord>> getSmsRecords(@PathVariable String batchId) {
        try {
            List<SmsRecord> records = smsService.getSmsRecordsByBatchId(batchId);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取短信发送统计
     */
    @GetMapping("/statistics/{batchId}")
    public ResponseEntity<String> getSmsStatistics(@PathVariable String batchId) {
        try {
            String statistics = smsService.getSmsStatistics(batchId);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("获取统计信息失败：" + e.getMessage());
        }
    }

    /**
     * 发送单条短信
     */
    @PostMapping("/single")
    public ResponseEntity<String> sendSingleSms(@RequestParam String phone, @RequestParam String content) {
        try {
            // 验证参数
            if (phone == null || phone.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("手机号不能为空");
            }
            if (content == null || content.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("短信内容不能为空");
            }

            boolean success = smsService.sendSingleSms(phone, content);
            
            if (success) {
                return ResponseEntity.ok("短信发送成功");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("短信发送失败");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("发送失败：" + e.getMessage());
        }
    }
}