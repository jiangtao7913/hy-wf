package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.repository.v1.InstallLogRepository;
import com.hy.wf.entity.InstallLog;
import com.hy.wf.entity.base.BaseEntity;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-08 11:27
 **/
@Repository
public class InstallLogRepositoryImpl extends BaseRepositoryImpl<InstallLog,Long> implements InstallLogRepository {
    private static final String TABLE_NAME = "t_install_log";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<InstallLog> getBeanClass() {
        return InstallLog.class;
    }


    @Override
    public InstallLog findByHardwareKeyAndDataStatus(String hardwareKey, BaseEntity.DataStatus  dataStatus) {
        return select().where(Condition.equal("hardware_key",hardwareKey)).comm().findOne();
    }

    @Override
    public int  updateById(Long id, String hardwareKey, String deviceModel, String ip, String version, Date date) {
        return update().set("hardware_key",hardwareKey).set("client_type",deviceModel)
                .set("ip",ip).set("client_version",version).set("modify_date",date)
                .where(Condition.equal("id",id)).update();
    }

    @Override
    public int updateFreeById(Long id, int freeTime) {
        return update().set("free_time",freeTime)
                .where(Condition.equal("id",id)).update();
    }

    @Override
    public InstallLog findByHardwareKeyAndModifyDate(String hardwareKey, String date) {
        return select().where(Condition.equal("hardware_key",hardwareKey)).
                where("DATE_FORMAT(modify_date,'%Y-%m-%d') = ?",date).comm().findOne();
    }
}
