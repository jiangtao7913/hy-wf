package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.PluginConfig;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-16 16:08
 **/
public interface PluginConfigRepository extends BaseRepository<PluginConfig,Long> {

    /** 
    * @Description: 根据插件名称查询插件
    * @Param [pluginName]
    * @return: com.hy.wf.dao.entity.v1.PluginConfig
    * @Author: jt 
    * @Date: 2019/1/16 
    */ 
    PluginConfig findByPluginName(String pluginName,String appType);

    /** 
    * @Description: 根据插件名称判断插件是否存在 
    * @Param [pluginName]
    * @return: int 
    * @Author: jt 
    * @Date: 2019/1/16 
    */ 
    int pluginNameExists(String pluginName,String appType);
}
