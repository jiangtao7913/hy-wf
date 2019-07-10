package com.hy.wf.admin.modules.service;

import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-21 15:39
 **/
public interface CommonService {

    PageUtils queryFunctionPage(Map<String, Object> params);

    List<OutPut> saveFunction(Function function);

    List<OutPut> updateFunction(Function function);

    Function selectFunctionById(Long id);

    List<OutPut> deleteFunction(Long[] ids,Function function);


    PageUtils queryAdPage(Map<String, Object> params);

    List<Ad> saveAd(Ad ad);

    List<Ad> updateAd(Ad ad);

    Ad selectAdById(Long id);

    List<Ad> deleteAd(Long[] ids,Ad ad);


    PageUtils queryAppLogPage(Map<String, Object> params);

    void deleteLog(Long[] ids);



    PageUtils queryConfigPage(Map<String, Object> params);

    String saveConfig(AppConfig appConfig);

    String updateConfig(AppConfig appConfig);

    AppConfig selectConfigById(Long id);

    void deleteConfig(Long[] ids,AppConfig appConfig);

    Map<String,String> saveConfigOther(AppConfig appConfig);

    Map<String,String> updateConfigOther(AppConfig appConfig);

    Map<String, String> deleteConfigOther(AppConfig appConfig);

    @Data
    class OutPut{
        private Function function;
        private FunctionRule functionRule;
    }
}
