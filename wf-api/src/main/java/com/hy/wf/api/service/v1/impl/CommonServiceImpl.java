package com.hy.wf.api.service.v1.impl;

import com.hy.wf.api.dao.repository.v1.AdRepository;
import com.hy.wf.api.dao.repository.v1.InstallLogRepository;
import com.hy.wf.api.service.v1.AppConfigService;
import com.hy.wf.api.service.v1.ChannelRecordService;
import com.hy.wf.api.service.v1.CommonService;
import com.hy.wf.api.service.v1.RecordService;
import com.hy.wf.common.Constant;
import com.hy.wf.common.DateUtils;
import com.hy.wf.common.Result;
import com.hy.wf.common.exception.ErrorCode;
import com.hy.wf.common.exception.ServiceException;
import com.hy.wf.entity.Ad;
import com.hy.wf.entity.AppConfig;
import com.hy.wf.entity.InstallLog;
import com.hy.wf.entity.Record;
import com.hy.wf.entity.base.BaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: hy-wf
 * @description: app公用service实现类
 * @author: jt
 * @create: 2019-01-08 10:10
 **/
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private InstallLogRepository installLogRepository;
    @Autowired
    private AppConfigService appConfigService;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private RecordService recordService;
    @Autowired
    private ChannelRecordService channelRecordService;


    /**
    * @Description: app运营安装接口
    * @Param [hardwareKey, deviceModel, ip, version, source, apkVersion]
    * @return: boolean
    * @Author: jt
    * @Date: 2019/1/8
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InstallLog install(String hardwareKey, String deviceModel, String ip, String version,String source,String appType) {

        InstallLog installLog = installLogRepository.findByHardwareKeyAndDataStatus(hardwareKey, BaseEntity.DataStatus.valid);

        int install = 0;
        if(null == installLog){
            install = 1;
        }
        //统计逻辑
        String date = DateUtils.format(new Date());
        recordService.record(date,hardwareKey,install,installLog);
        channelRecordService.recordChannel(date,appType,source,hardwareKey,install,installLog);

        if(installLog !=null){
            installLogRepository.updateById(installLog.getId(),hardwareKey,deviceModel,ip,version,new Date());
        }else {
            installLog = new InstallLog();
            installLog.setHardwareKey(hardwareKey);
            installLog.setIp(ip);
            installLog.setClientType(deviceModel);
            installLog.setClientVersion(version);
            installLog.setMemo("");
            installLog.setSource(source);
            installLog.setAppType(appType);
//           installLog.setFreeTime(Constant.FREE_TIME);
//           installLog.setFreeCount(Constant.FREE_COUNT);
            installLog.init();
            KeyHolder keyHolder = new GeneratedKeyHolder();
            installLogRepository.save(installLog,keyHolder);
        }
        return installLog;
    }

//    /**
//    * @Description: 修改app免费试用次数
//    * @Param [hardwareKey]
//    * @return: boolean
//    * @Author: jt
//    * @Date: 2019/2/26
//    */
//    @Override
//    public int updateInstall(String hardwareKey) {
//        InstallLog installLog = installLogRepository.findByHardwareKeyAndDataStatus(hardwareKey, BaseEntity.DataStatus.valid);
//        if(installLog ==null){
//            throw new ServiceException(ErrorCode.C4001);
//        }
//        //int freeTime = installLog.getFreeTime()-1;
//        installLogRepository.updateFreeById(installLog.getId(),freeTime);
//        return freeTime;
//    }

    /**
    * @Description: 根据module获取app配置信息
    * @Param [module,version,type]
    * @return: java.util.List<com.hy.wf.dao.entity.v1.AppConfigRepository>
    * @Author: jt
    * @Date: 2019/1/8
    */
    @Override
    public Map<String,Object> version(String module,String version,String type,String appType) {
        Map<String, Object> map = new HashMap<>(4);
        Integer lastVersion = null;
        String key = null;
        if(StringUtils.equalsIgnoreCase(Constant.ANDROID,type)){
            key = appConfigService.getValue(module,"version.android",appType);
            if(null == key){
                throw new ServiceException(ErrorCode.C2001);
            }
            lastVersion = Integer.valueOf(key.replace(".", ""));
        }else if (StringUtils.equalsIgnoreCase(Constant.IOS,type)){
            key = appConfigService.getValue(module,"version.ios",appType);
            if(null == key){
                throw new ServiceException(ErrorCode.C2001);
            }
            lastVersion = Integer.valueOf(key.replace(".", ""));
        }
        Integer currentVersion =Integer.valueOf(version.replace(".", ""));
        Boolean forceUpdate = false;

        if(lastVersion >currentVersion){
            String downloadUrl = appConfigService.getValue(module,"package.download.url",appType);
            String[] updateFeatures = new String[0];
            if (StringUtils.equalsIgnoreCase(Constant.ANDROID, type)) {
                forceUpdate = Boolean.valueOf(appConfigService.getValue(module,"force.update.andorid",appType));
                updateFeatures = appConfigService.getValue(module,"force.feature.andorid",appType).split(Constant.SEPARATOR);
                downloadUrl += appConfigService.getValue(module,"package.url.andorid",appType);
            } else if (StringUtils.equalsIgnoreCase(Constant.IOS, type)){
                forceUpdate = Boolean.valueOf(appConfigService.getValue(module,"force.update.ios",appType));
                updateFeatures = appConfigService.getValue(module,"force.feature.ios",appType).split(Constant.SEPARATOR);
                downloadUrl  += appConfigService.getValue(module,"package.url.ios",appType);
            }
            map.put("updateFeatures", updateFeatures);
            map.put("downloadUrl", downloadUrl);
            map.put("lastVersion", key);
            map.put("forceUpdate", forceUpdate);
            return map;
        }
        return null;
    }

    /** 
    * @Description: 根据位置查询有效时间内的广告
    * @Param [position, startDate, endDate]
    * @return: java.util.List<com.hy.wf.dao.entity.v1.Ad>
    * @Author: jt 
    * @Date: 2019/1/9 
    */ 
    @Override
    @Cacheable(value = "ad",key = "#appType+'-'+#position")
    public List<Ad> getByPositionAndAppType(String position, Date startDate, Date endDate,String appType) {
        return adRepository.findByPositionBetweenStartDateAndEndDateAndAppType(position,startDate,endDate,appType);
    }

    @Override
    @Cacheable(value = "config",key = "#appType+'-'+#module")
    public Map<String,String> getAppConfig(String module, String appType) {
        Map<String,String> map = new HashMap<>();
        List<AppConfig> list = appConfigService.findByModelAndAppType(module,appType);
        for (AppConfig appConfig : list){
            map.put(appConfig.getName(),appConfig.getValue());
        }
       return map;
    }


}
