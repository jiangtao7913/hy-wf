package com.hy.wf.api.service.v1.impl;
import java.math.BigDecimal;

import com.hy.wf.api.dao.repository.v1.InstallLogRepository;
import com.hy.wf.api.dao.repository.v1.RecordRepository;
import com.hy.wf.api.service.v1.RecordService;
import com.hy.wf.common.DateUtils;
import com.hy.wf.entity.InstallLog;
import com.hy.wf.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-25 18:30
 **/
@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private InstallLogRepository installLogRepository;


    @Override
    public Record findByDate(String date) {
        return recordRepository.findByDate(date);
    }

    @Override
    public void record(String date,String hardwareKey,int install,InstallLog installLog) {
        Record record = findByDate(date);
        if(record == null ){
            record = new Record();
            record.setDate(date);
            record.setTotalIncome(BigDecimal.ZERO);
            record.setWxIncome(BigDecimal.ZERO);
            record.setZfbIncome(BigDecimal.ZERO);
            record.setBalanceIncome(BigDecimal.ZERO);
            record.setTotalDeduct(BigDecimal.ZERO);
            record.setTotalInstall(install);
            record.setDayActiveCount(1);
            record.init();
            recordRepository.save(record);
        }else {
            //统计安装人数
            if(null == installLog){
                record.setTotalInstall(record.getTotalInstall()+1);
                recordRepository.updateColumnById("total_install",String.valueOf(record.getTotalInstall()),record.getId());
            }

            //统计日活
            InstallLog installLog1 = installLogRepository.findByHardwareKeyAndModifyDate(hardwareKey,date);
            if(null == installLog1){
                record.setDayActiveCount(record.getDayActiveCount()+1);
                recordRepository.updateColumnById("day_active_count",String.valueOf(record.getDayActiveCount()),record.getId());
            }
        }
    }

    @Override
    public void updateRecord(String date,BigDecimal totalIncome,BigDecimal wxIncome,BigDecimal zfbIncome,BigDecimal balanceIncome
    ,BigDecimal totalDeduct) {
        Record record = findByDate(date);
        record.setTotalIncome(record.getTotalIncome().add(totalIncome));
        record.setWxIncome(record.getWxIncome().add(wxIncome));
        record.setZfbIncome(record.getZfbIncome().add(zfbIncome));
        record.setBalanceIncome(record.getBalanceIncome().add(balanceIncome));
        record.setTotalDeduct(record.getTotalDeduct().add(totalDeduct));
        recordRepository.updateById(record);
    }


}
