package com.hy.wf.admin.modules.service;

import com.baomidou.mybatisplus.service.IService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.PluginConfigAttribute;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-26 15:05
 **/
public interface PluginAttributeService extends IService<PluginConfigAttribute> {

    PageUtils queryPage(Map<String, Object> params);

    void save(PluginConfigAttribute pluginConfigAttribute);

    void update(PluginConfigAttribute pluginConfigAttribute);

    void delete(Long[] ids);

    List<Map<String,String>> getPluginName();


}
