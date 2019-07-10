package com.hy.wf.admin.modules.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hy.wf.entity.PluginConfigAttribute;

import java.util.List;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-26 15:07
 **/
public interface PluginConfigAttributeDao extends BaseMapper<PluginConfigAttribute> {

    List<Map<String,String>> queryPluginNameList();
}
