package com.company.ems.service.impl;

import com.company.ems.dto.SmsBatchDTO;
import com.company.ems.entity.Employee;
import com.company.ems.entity.SmsRecord;
import com.company.ems.mapper.EmployeeMapper;
import com.company.ems.mapper.SmsRecordMapper;
import com.company.ems.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsRecordMapper smsRecordMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    @Async("smsTaskExecutor")
    public CompletableFuture<String> sendBatchSms(SmsBatchDTO smsBatchDTO) {
        String batchId = smsBatchDTO.getBatchId();
        if (StringUtils.isEmpty(batchId)) {
            batchId = UUID.randomUUID().toString();
        }

        try {
            List<Employee> employees = employeeMapper.selectByIds(smsBatchDTO.getEmployeeIds());
            
            List<SmsRecord> smsRecords = new ArrayList<>();
            for (Employee employee : employees) {
                SmsRecord record = new SmsRecord();
                record.setBatchId(batchId);
                record.setEmployeeId(employee.getId());
                record.setPhone(employee.getPhone());
                record.setContent(smsBatchDTO.getContent());
                record.setStatus(0);
                smsRecords.add(record);
            }

            for (SmsRecord record : smsRecords) {
                smsRecordMapper.insert(record);
            }

            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (SmsRecord record : smsRecords) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    processSingleSms(record);
                });
                futures.add(future);
            }

            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                    futures.toArray(new CompletableFuture[0])
            );
            allFutures.get();

            return CompletableFuture.completedFuture("批次 " + batchId + " 发送完成！共发送 " + smsRecords.size() + "条短信");

        } catch (Exception e) {
            return CompletableFuture.completedFuture("批次 " + batchId + " 发送失败：" + e.getMessage());
        }
    }

    private void processSingleSms(SmsRecord record) {
        try {
            boolean success = sendSingleSms(record.getPhone(), record.getContent());
            
            record.setStatus(success ? 1 : 2);
            record.setSendTime(LocalDateTime.now());
            if (!success) {
                record.setErrorMessage("发送失败");
            }
            
            smsRecordMapper.updateById(record);
            
        } catch (Exception e) {
            record.setStatus(2);
            record.setSendTime(LocalDateTime.now());
            record.setErrorMessage(e.getMessage());
            smsRecordMapper.updateById(record);
        }
    }

    @Override
    public boolean sendSingleSms(String phone, String content) {
        try {
            Thread.sleep(1000 + (long)(Math.random() * 2000));
            return Math.random() > 0.1;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public List<SmsRecord> getSmsRecordsByBatchId(String batchId) {
        return smsRecordMapper.selectByBatchId(batchId);
    }

    @Override
    public String getSmsStatistics(String batchId) {
        List<SmsRecord> records = smsRecordMapper.selectByBatchId(batchId);

        long total = records.size();
        long success = records.stream().filter(r -> r.getStatus() == 1).count();
        long failed = records.stream().filter(r -> r.getStatus() == 2).count();
        long pending = records.stream().filter(r -> r.getStatus() == 0).count();

        return String.format("批次 %s 统计：总计 %d 条，成功 %d 条，失败 %d 条，待发送 %d 条",
                batchId, total, success, failed, pending);
    }
}
