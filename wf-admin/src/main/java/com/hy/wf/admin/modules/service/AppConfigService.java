package com.hy.wf.admin.modules.service;

import com.baomidou.mybatisplus.service.IService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.Ad;
import com.hy.wf.entity.AppConfig;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

import java.util.List;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-25 10:09
 **/
public interface AppConfigService extends IService<AppConfig> {

    PageUtils queryPage(Map<String, Object> params);

    void save(AppConfig appConfig);

    void update(AppConfig appConfig);

    AppConfig findByModelAndAppTypeAndName(String model,String appType,String name);

    void deleteRedis(AppConfig appConfig);

    List<AppConfig> updateRedis(List<AppConfig> list, AppConfig ad);
}
