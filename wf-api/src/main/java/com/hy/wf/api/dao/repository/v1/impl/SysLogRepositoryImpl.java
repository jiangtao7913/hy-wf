package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.repository.v1.AppLogRepository;
import com.hy.wf.entity.AppLog;
import org.springframework.stereotype.Repository;

/**
 * @program: hy-wf
 * @description: 系统日志
 * @author: jt
 * @create: 2019-01-09 18:23
 **/
@Repository
public class SysLogRepositoryImpl extends BaseRepositoryImpl<AppLog,Long> implements AppLogRepository {
    private static final String TABLE_NAME = "app_log";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<AppLog> getBeanClass() {
        return AppLog.class;
    }
}
