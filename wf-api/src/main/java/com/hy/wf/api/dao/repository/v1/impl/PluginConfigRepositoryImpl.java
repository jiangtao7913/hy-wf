package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.repository.v1.PluginConfigRepository;
import com.hy.wf.entity.PluginConfig;
import org.springframework.stereotype.Repository;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-16 16:09
 **/
@Repository
public class PluginConfigRepositoryImpl extends BaseRepositoryImpl<PluginConfig,Long> implements PluginConfigRepository {
    private static final String TABLE_NAME = "t_plugin_config";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<PluginConfig> getBeanClass() {
        return PluginConfig.class;
    }

    @Override
    public PluginConfig findByPluginName(String pluginName,String appType) {
        return select().where(Condition.equal("plugin_name",pluginName))
                .where(Condition.equal("app_type",appType)).comm().findOne();
    }

    @Override
    public int pluginNameExists(String pluginName,String appType) {
        return select().where(Condition.equal("plugin_name",pluginName))
                .where(Condition.equal("app_type",appType)).comm().findCount();
    }
}
