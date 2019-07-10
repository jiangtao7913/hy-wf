package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.repository.v1.PluginConfigAttributeRepository;
import com.hy.wf.entity.PluginConfigAttribute;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-17 15:54
 **/
@Repository
public class PluginConfigAttributeRepositoryImpl extends BaseRepositoryImpl<PluginConfigAttribute,Long> implements PluginConfigAttributeRepository {
    private static final String TABLE_NAME = "t_plugin_config_attribute";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<PluginConfigAttribute> getBeanClass() {
        return PluginConfigAttribute.class;
    }

    /**
     * 根据插件名称查找插件属性
     * @param pluginId
     * @return
     */
    @Override
    public List<PluginConfigAttribute> findByPluginId(Long pluginId) {
        return select().where(Condition.equal("plugin_config_id",pluginId)).comm().find();
    }
}
