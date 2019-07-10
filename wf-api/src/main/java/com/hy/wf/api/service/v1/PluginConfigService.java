package com.hy.wf.api.service.v1;

import com.hy.wf.entity.PluginConfig;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-16 16:12
 **/

public interface PluginConfigService {

    /**
     * 判断插件ID是否存在
     *
     * @param pluginName 插件ID
     * @return 插件ID是否存在
     */
    boolean pluginIdExists(String pluginName,String appType);

    /**
     * 根据插件ID查找插件配置
     *
     * @param pluginName 插件ID
     * @return 插件配置，若不存在则返回null
     */
    PluginConfig findByPluginName(String pluginName,String appType);
}
