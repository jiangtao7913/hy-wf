package com.hy.wf.api.service.v1.impl;

import com.hy.wf.api.dao.repository.v1.AppConfigRepository;
import com.hy.wf.api.service.v1.AppConfigService;
import com.hy.wf.entity.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: hy-wf
 * @description: 配置信息服务
 * @author: jt
 * @create: 2019-01-08 17:08
 **/
@Service
public class AppConfigServiceImpl implements AppConfigService {

    @Autowired
    private AppConfigRepository appConfigRepository;

    /**
    * @Description: 获取配置信息
    * @Param [module, name]
    * @return: java.lang.String
    * @Author: jt
    * @Date: 2019/1/8
    */
    @Override
    @Cacheable(value = "config",key = "#appType+'-'+#name")
    public String getValue(String module, String name,String appType) {
        AppConfig appConfig = appConfigRepository.findByModelAndNameAndAppType(module,name,appType);
        return appConfig ==null?null:appConfig.getValue();
    }

    /**
    * @Description: 设置配置信息
    * @Param [module,name, value]
    * @return: boolean
    * @Author: jt
    * @Date: 2019/1/8
    */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    @CachePut(value = "config",key = "#appType+'-'+#name")
    public boolean setValue(String module,String name, String value,String appType) {
       AppConfig appConfig =  appConfigRepository.findByModelAndNameAndAppType(module,name,appType);
       if(null == appConfig){
           appConfig = new AppConfig();
           appConfig.setName(name);
           appConfig.setValue(value);
           appConfig.setModel(module);
           appConfig.setRemark("");
           appConfig.init();
           appConfigRepository.save(appConfig);
       }else {
           appConfigRepository.updateValueById(appConfig.getId(),value);
       }
        return true;
    }

    @Override
    public List<AppConfig> findByModelAndAppType(String model, String appType) {
        return appConfigRepository.findByModelAndAppType(model,appType);
    }


}
