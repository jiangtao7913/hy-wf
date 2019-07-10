package com.hy.wf.admin.modules.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hy.wf.admin.common.utils.Constant;
import com.hy.wf.admin.common.utils.Query;
import com.hy.wf.admin.modules.dao.PluginConfigAttributeDao;
import com.hy.wf.admin.modules.service.PluginAttributeService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.Function;
import com.hy.wf.entity.PluginConfigAttribute;
import com.hy.wf.entity.base.BaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-26 15:06
 **/
@Service
public class PluginAttributeServiceImpl extends ServiceImpl<PluginConfigAttributeDao, PluginConfigAttribute> implements PluginAttributeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String title = (String)params.get("title");

        Page<PluginConfigAttribute> page = this.selectPage(
                new Query<PluginConfigAttribute>(params).getPage(),
                new EntityWrapper<PluginConfigAttribute>()
                        .eq("data_status", BaseEntity.DataStatus.valid.value)
                        .like(StringUtils.isNotBlank(title),"title", title)
                        .orderBy(!StringUtils.isNotBlank(params.get("sidx").toString()), Arrays.asList("id"),false)
                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );
        return new PageUtils(page);
    }

    @Override
    public void save(PluginConfigAttribute pluginConfigAttribute) {
        this.insert(pluginConfigAttribute);
    }

    @Override
    public void update(PluginConfigAttribute pluginConfigAttribute) {
        this.updateById(pluginConfigAttribute);
    }

    @Override
    public void delete(Long[] ids) {
        this.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public List<Map<String,String>> getPluginName() {
        List<Map<String,String>> mapList = baseMapper.queryPluginNameList();
         return mapList;
    }

}
