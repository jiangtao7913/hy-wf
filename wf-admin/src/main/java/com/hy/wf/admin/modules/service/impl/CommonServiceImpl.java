package com.hy.wf.admin.modules.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hy.wf.admin.modules.service.*;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.Ad;
import com.hy.wf.entity.AppConfig;
import com.hy.wf.entity.Function;
import com.hy.wf.entity.FunctionRule;
import com.hy.wf.entity.base.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-21 15:40
 **/
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private FunctionService functionService;
    @Autowired
    private FunctionRuleService functionRuleService;
    @Autowired
    private AdService adService;
    @Autowired
    private AppLogService appLogService;
    @Autowired
    private AppConfigService appConfigService;


    @Override
    public PageUtils queryFunctionPage(Map<String, Object> params) {
        return functionService.queryPage(params);
    }

    /**
     * 新增数据同步redis
     * @param function
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "function",key = "#function.getAppType()+'-'+#function.getPosition()")
    public List<OutPut> saveFunction(Function function) {
        functionService.save(function);
        return ruleCommon(function);
    }

    /**
     * 修改数据同步redis
     * @param function
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "function",key = "#function.getAppType()+'-'+#function.getPosition()")
    public List<OutPut> updateFunction(Function function) {
        functionService.update(function);
        return ruleCommon(function);
    }

    private List<OutPut> ruleCommon(Function function){
        List<Function> list = functionService.findByPositionAndAppType(function.getPosition(),function.getAppType());
        if(null == list){
            return null;
        }
        List<Long> ids = list.stream().map(Function::getId).collect(Collectors.toList());
        List<FunctionRule> functionRuleList = functionRuleService.findByFunctionIds(ids);
        Map<Long,FunctionRule> map = functionRuleList.stream().collect(Collectors.toMap(FunctionRule::getFunctionId, java.util.function.Function.identity()));
        List<OutPut> outPutList = new ArrayList<>();
        for(Function functionItem : list){
            OutPut outPut = new OutPut();
            outPut.setFunction(functionItem);
            outPut.setFunctionRule(map.get(functionItem.getId()));
            outPutList.add(outPut);
        }
        return outPutList;
    }

    @Override
    public Function selectFunctionById(Long id) {
        return functionService.selectById(id);
    }

    /**
     * 删除数据同步redis
     * @param ids
     * @param function
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OutPut> deleteFunction(Long[] ids,Function function) {
        functionService.deleteBatchIds(Arrays.asList(ids));
        List<Function> list = functionService.findByPositionAndAppType(function.getPosition(),function.getAppType());
        if(list.isEmpty()){
            functionService.deleteRedis(function);
            return null;
        }else {
            List<OutPut> outPutList = ruleCommon(function);
            return functionService.updateRedis(outPutList,function);
        }
    }

    @Override
    public PageUtils queryAdPage(Map<String, Object> params) {
        return adService.queryPage(params);
    }

    @Override
    @CachePut(value = "ad",key = "#ad.getAppType()+'-'+#ad.getPosition()")
    public List<Ad> saveAd(Ad ad) {
        adService.save(ad);
        List<Ad> adList = adService.findByPositionAndAppType(ad.getPosition(),ad.getAppType());
        return adList;
    }

    @Override
    @CachePut(value = "ad",key = "#ad.getAppType()+'-'+#ad.getPosition()")
    public List<Ad> updateAd(Ad ad) {
        adService.update(ad);
        List<Ad> adList = adService.findByPositionAndAppType(ad.getPosition(),ad.getAppType());
        return adList;
    }

    @Override
    public Ad selectAdById(Long id) {
        return adService.selectById(id);
    }

    @Override
    public List<Ad> deleteAd(Long[] ids, Ad ad) {
        adService.deleteBatchIds(Arrays.asList(ids));
        List<Ad> list = adService.findByPositionAndAppType(ad.getPosition(),ad.getAppType());
        if(list.isEmpty()){
            adService.deleteRedis(ad);
            return null;
        }else {
            adService.updateRedis(list,ad);
            return list;
        }
    }

    @Override
    public PageUtils queryAppLogPage(Map<String, Object> params) {
        return appLogService.queryPage(params);
    }

    @Override
    public void deleteLog(Long[] ids) {
        appLogService.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public PageUtils queryConfigPage(Map<String, Object> params) {
        return appConfigService.queryPage(params);
    }

    @Override
    @CachePut(value = "config",key = "#appConfig.getAppType()+'-'+#appConfig.getName()")
    @Transactional(rollbackFor = Exception.class)
    public String saveConfig(AppConfig appConfig) {
        appConfigService.save(appConfig);
        //AppConfig a = appConfigService.findByModelAndAppTypeAndName(appConfig.getModel(),appConfig.getAppType(),appConfig.getName());
        return appConfig.getValue();
    }

    @Override
    @CachePut(value = "config",key = "#appConfig.getAppType()+'-'+#appConfig.getName()")
    @Transactional(rollbackFor = Exception.class)
    public String updateConfig(AppConfig appConfig) {
        appConfigService.update(appConfig);
        //List<AppConfig> appConfigList = appConfigService.findByModelAndAppType(appConfig.getModel(),appConfig.getAppType());
        return appConfig.getValue();
    }

    @Override
    public AppConfig selectConfigById(Long id) {
        return appConfigService.selectById(id);
    }

    @Override
    @CacheEvict(value="config",key = "#appConfig.getAppType()+'-'+#appConfig.getName()")
    public void deleteConfig(Long[] ids, AppConfig appConfig) {
        appConfigService.deleteBatchIds(Arrays.asList(ids));
        deleteConfigOther(appConfig);
//        List<AppConfig> list = appConfigService.findByModelAndAppType(appConfig.getModel(),appConfig.getAppType());
//        if(list.isEmpty()){
//           appConfigService.deleteRedis(appConfig);
//            return null;
//        }else {
//            appConfigService.updateRedis(list,appConfig);
//            return list;
//        }
    }

    @Override
    @CachePut(value = "config",key = "#appConfig.getAppType()+'-'+#appConfig.getModel()")
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> saveConfigOther(AppConfig appConfig) {
        appConfigService.save(appConfig);
        Map<String,String> map = new HashMap<>();
        List<AppConfig> list = getAppConfigList(appConfig);
        for (AppConfig appConfig1 : list){
            map.put(appConfig1.getName(),appConfig1.getValue());
        }
        return map;
    }

    @Override
    @CachePut(value = "config",key = "#appConfig.getAppType()+'-'+#appConfig.getModel()")
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> updateConfigOther(AppConfig appConfig) {
        appConfigService.update(appConfig);
        Map<String,String> map = new HashMap<>();
        List<AppConfig> list = getAppConfigList(appConfig);
        for (AppConfig appConfig1 : list){
            map.put(appConfig1.getName(),appConfig1.getValue());
        }
        return map;
    }

    @Override
    @CacheEvict(value="config",key = "#appConfig.getAppType()+'-'+#appConfig.getModel()")
    public Map<String, String> deleteConfigOther(AppConfig appConfig) {
        Map<String,String> map = new HashMap<>();
        List<AppConfig> list = getAppConfigList(appConfig);
        for (AppConfig appConfig1 : list){
            map.put(appConfig1.getName(),appConfig1.getValue());
        }
        return map;
    }

    private List<AppConfig> getAppConfigList(AppConfig appConfig){
        List<AppConfig> list = appConfigService.selectList(new EntityWrapper<AppConfig>()
                .eq("model",appConfig.getModel())
                .eq("app_type",appConfig.getAppType())
                .eq("data_status",AppConfig.DataStatus.valid.value)
        );
        return list;
    }

}
