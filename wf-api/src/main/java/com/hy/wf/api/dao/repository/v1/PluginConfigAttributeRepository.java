package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.PluginConfigAttribute;

import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-17 15:53
 **/
public interface PluginConfigAttributeRepository extends BaseRepository<PluginConfigAttribute,Long> {

    /** 
    * @Description: 根据插件id查找插件属性
    * @Param [pluginId]
    * @return: java.util.List<com.hy.wf.dao.entity.v1.PluginConfigAttribute>
    * @Author: jt 
    * @Date: 2019/1/17 
    */ 
    List<PluginConfigAttribute> findByPluginId(Long pluginId);
}
