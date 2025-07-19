
package com.company.ems.mapper;

import com.company.ems.entity.SmsRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SmsRecordMapper {

    List<SmsRecord> selectByBatchId(@Param("batchId")
                                    String batchId);

    SmsRecord selectById(@Param("id") Long id);

    int insert(SmsRecord smsRecord);

    int batchInsert(@Param("list") List<SmsRecord>
                            smsRecords);

    int updateById(SmsRecord smsRecord);

    List<SmsRecord> selectPendingRecords();
}
