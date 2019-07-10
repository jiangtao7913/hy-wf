package com.hy.wf.api.service.v1.impl;

import com.hy.wf.api.dao.repository.v1.PluginConfigAttributeRepository;
import com.hy.wf.api.dao.repository.v1.PluginConfigRepository;
import com.hy.wf.api.service.v1.PluginConfigService;
import com.hy.wf.entity.PluginConfig;
import com.hy.wf.entity.PluginConfigAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: hy-wf
 * @description: 插件
 * @author: jt
 * @create: 2019-01-16 16:15
 **/
@Service
public class PluginConfigServiceImpl implements PluginConfigService {

    @Autowired
    private PluginConfigRepository pluginConfigRepository;
    @Autowired
    private PluginConfigAttributeRepository pluginConfigAttributeRepository;

    @Override
    @Transactional(rollbackFor = Exception.class,readOnly = true)
    public boolean pluginIdExists(String pluginName,String appType) {
       int count = pluginConfigRepository.pluginNameExists(pluginName,appType);
       return count>=1 ? true:false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class,readOnly = true)
    public PluginConfig findByPluginName(String pluginName,String appType) {
        PluginConfig pluginConfig = pluginConfigRepository.findByPluginName(pluginName,appType);
        if (pluginConfig != null) {
            List<PluginConfigAttribute> pluginConfigAttributes = pluginConfigAttributeRepository.findByPluginId(pluginConfig.getId());
            for (PluginConfigAttribute pluginConfigAttribute : pluginConfigAttributes) {
                pluginConfig.addAttribute(pluginConfigAttribute.getName(), pluginConfigAttribute.getValue());
            }
        }
        return pluginConfig;
    }

}
