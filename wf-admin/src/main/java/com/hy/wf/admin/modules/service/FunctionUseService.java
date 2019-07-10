package com.hy.wf.admin.modules.service;

import com.baomidou.mybatisplus.service.IService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.FunctionUse;
import com.hy.wf.entity.InstallLog;

import java.util.Map;

public interface FunctionUseService extends IService<FunctionUse> {

    PageUtils queryPage(Map<String, Object> params);
}
