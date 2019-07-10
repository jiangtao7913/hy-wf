package com.hy.wf.admin.modules.service;

import com.baomidou.mybatisplus.service.IService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.admin.modules.dao.entity.PluginConfig;


import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-26 14:59
 **/
public interface PluginService extends IService<PluginConfig> {

    PageUtils queryPage(Map<String, Object> params);

    void save(PluginConfig pluginConfig);

    void update(PluginConfig pluginConfig);

    void delete(Long[] ids);
}
