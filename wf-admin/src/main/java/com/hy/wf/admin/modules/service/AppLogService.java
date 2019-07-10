package com.hy.wf.admin.modules.service;

import com.baomidou.mybatisplus.service.IService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.AppLog;

import java.util.Map;

public interface AppLogService extends IService<AppLog> {

    PageUtils queryPage(Map<String, Object> params);
}
