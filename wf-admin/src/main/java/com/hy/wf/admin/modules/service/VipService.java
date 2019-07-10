package com.hy.wf.admin.modules.service;

import com.baomidou.mybatisplus.service.IService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.InstallLog;
import com.hy.wf.entity.Vip;

import java.util.Map;

public interface VipService extends IService<Vip> {

    PageUtils queryPage(Map<String, Object> params);

    void save(Vip vip);
}
