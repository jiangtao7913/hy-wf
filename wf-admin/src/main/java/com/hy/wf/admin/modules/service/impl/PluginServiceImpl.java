package com.hy.wf.admin.modules.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hy.wf.admin.common.utils.Constant;
import com.hy.wf.admin.common.utils.Query;
import com.hy.wf.admin.modules.dao.PluginConfigDao;
import com.hy.wf.admin.modules.service.PluginService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.admin.modules.dao.entity.PluginConfig;
import com.hy.wf.entity.base.BaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-26 15:03
 **/
@Service
public class PluginServiceImpl extends ServiceImpl<PluginConfigDao, PluginConfig> implements PluginService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String title = (String)params.get("title");

        Page<PluginConfig> page = this.selectPage(
                new Query<PluginConfig>(params).getPage(),
                new EntityWrapper<PluginConfig>()
                        .eq("data_status", BaseEntity.DataStatus.valid.value)
                        .like(StringUtils.isNotBlank(title),"title", title)
                        .orderBy(!StringUtils.isNotBlank(params.get("sidx").toString()), Arrays.asList("id"),false)
                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );
        return new PageUtils(page);
    }

    @Override
    public void save(PluginConfig pluginConfig) {
        pluginConfig.init();
        this.insert(pluginConfig);
    }

    @Override
    public void update(PluginConfig pluginConfig) {
        this.updateById(pluginConfig);
    }

    @Override
    public void delete(Long[] ids) {
        this.deleteBatchIds(Arrays.asList(ids));
    }
}
