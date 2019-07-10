package com.hy.wf.admin.modules.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hy.wf.admin.common.utils.Constant;
import com.hy.wf.admin.common.utils.Query;
import com.hy.wf.admin.modules.dao.AppConfigDao;
import com.hy.wf.admin.modules.service.AppConfigService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.AppConfig;
import com.hy.wf.entity.base.BaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-25 10:10
 **/
@Service
public class AppConfigServiceImpl extends ServiceImpl<AppConfigDao, AppConfig> implements AppConfigService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String model = (String)params.get("model");
        String appType = (String)params.get("appType");

        Page<AppConfig> page = this.selectPage(
                new Query<AppConfig>(params).getPage(),
                new EntityWrapper<AppConfig>()
                        .eq(StringUtils.isNotBlank(model),"model", model)
                        .eq(StringUtils.isNotBlank(appType),"app_type", appType)
                        .eq("data_status", BaseEntity.DataStatus.valid.value)
                        .orderBy(!StringUtils.isNotBlank(params.get("sidx").toString()), Arrays.asList("id"),false)
                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );
        return new PageUtils(page);
    }

    @Override
    public void save(AppConfig appConfig) {
        appConfig.init();
        this.insert(appConfig);
    }

    @Override
    public void update(AppConfig appConfig) {
        this.updateById(appConfig);
    }

    @Override
    public AppConfig findByModelAndAppTypeAndName(String model,String appType,String name) {
        return this.selectOne(new EntityWrapper<AppConfig>()
                .eq("model",model)
                .eq("app_type",appType)
                .eq("name",name)
        );
    }

    @Override
    @CacheEvict(value="config",key = "#appConfig.getAppType()+'-'+#appConfig.getName()")
    public void deleteRedis(AppConfig appConfig) {

    }

    @Override
    @CachePut(value = "config",key = "#appConfig.getAppType()+'-'+#appConfig.getName()")
    public List<AppConfig> updateRedis(List<AppConfig> list, AppConfig appConfig) {
        return list;
    }

}
