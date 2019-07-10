package com.hy.wf.admin.modules.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hy.wf.admin.common.utils.Constant;
import com.hy.wf.admin.common.utils.Query;
import com.hy.wf.admin.modules.dao.AppLogDao;
import com.hy.wf.admin.modules.dao.InstallLogDao;
import com.hy.wf.admin.modules.service.AppLogService;
import com.hy.wf.admin.modules.service.InstallService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.AppLog;
import com.hy.wf.entity.InstallLog;
import com.hy.wf.entity.base.BaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-22 18:52
 **/
@Service
public class InstallServiceImpl extends ServiceImpl<InstallLogDao, InstallLog> implements InstallService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String hardwareKey = (String)params.get("hardwareKey");

        Page<InstallLog> page = this.selectPage(
                new Query<InstallLog>(params).getPage(),
                new EntityWrapper<InstallLog>()
                        .eq(StringUtils.isNotBlank(hardwareKey),"hardware_key", hardwareKey)
                        .eq("data_status", BaseEntity.DataStatus.valid.value)
                        .orderBy(!StringUtils.isNotBlank(params.get("sidx").toString()), Arrays.asList("id"),false)
                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );
        return new PageUtils(page);
    }
}
