package com.hy.wf.api.web.v1;

import com.hy.wf.api.dao.base.log.CustomLog;
import com.hy.wf.api.service.upload.FtpPlugin;
import com.hy.wf.api.service.v1.CommonService;
import com.hy.wf.api.service.v1.SmsService;
import com.hy.wf.api.service.v1.VerifyCodeService;
import com.hy.wf.api.web.BaseController;
import com.hy.wf.common.Result;
import com.hy.wf.common.annotation.IsLogin;
import com.hy.wf.common.exception.ErrorCode;
import com.hy.wf.common.exception.ServiceException;
import com.hy.wf.common.util.IPUtils;
import com.hy.wf.entity.Ad;
import com.hy.wf.entity.InstallLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @program: hy-wf
 * @description: APP通用api
 * @author: jt
 * @create: 2019-01-08 09:21
 **/
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController extends BaseController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Autowired
    private FtpPlugin ftpPlugin;

    /**
     * app安装记录接口
     */
    @PostMapping("/sys/install")
    @IsLogin
    public Result appInstall(@RequestHeader("hardwareKey")String hardwareKey, @RequestHeader("deviceModel")String deviceModel,
                             @RequestHeader("version")String version, @RequestHeader("source")String source,
                             @RequestHeader("appType")String appType){
        InstallLog installLog = commonService.install(hardwareKey,deviceModel, IPUtils.getIpAddr(getRequest()),version,source,appType);
        Map<String, Object> dataMap = new HashMap<>(1);
        dataMap.put("result",true);
//        dataMap.put("freeTime",installLog.getFreeTime());
//        dataMap.put("freeCount",installLog.getFreeCount());
        return  Result.success(dataMap);
    }

//    /**
//     * 修改app免费次数
//     */
//    @PostMapping("/sys/updateInstall")
//    @IsLogin
//    public Result updateInstall(@RequestHeader("hardwareKey")String hardwareKey){
//        int freeTime =  commonService.updateInstall(hardwareKey);
//        Map<String, Object> dataMap = new HashMap<String, Object>(1);
//        dataMap.put("freeTime",freeTime);
//        return  Result.success(dataMap);
//    }


    /**
     * 系统更新接口
     */
    @PostMapping("/sys/{module}")
    @IsLogin
    @CustomLog
    public Result version(@PathVariable("module")String module, @RequestHeader("version")String version,
                          @RequestHeader("type")String type,@RequestHeader("appType")String appType){
        Map<String,Object> map = commonService.version(module,version,type,appType);
        return  Result.success(map);
    }

    /**
     * app广告接口
     */
    @PostMapping("/ad/{position}")
    @IsLogin
    public Result getByPosition(@PathVariable("position")String position,@RequestHeader("appType")String appType){
        Date date = new Date();
        List<Ad> ads = commonService.getByPositionAndAppType(position,date,date,appType);
        return  Result.success(ads);
    }

    /**
     * 发送验证码接口
     */
    @PostMapping("/{mobile}")
    @IsLogin
    public Result setCode(@PathVariable("mobile")String mobile){
        //控制验证码一天发送10次，每发送一次记录到redis
        long date = System.currentTimeMillis();
        String max = verifyCodeService.getMax(mobile);
        String [] arrs = max.split("-");
        //次数
        int count = Integer.parseInt(arrs[0]);
        //时间
        long time  = Long.parseLong(arrs[1]);
        if(date-time<TimeUnit.DAYS.toMillis(1)){
            if(count >9){
                throw new ServiceException(ErrorCode.C1007);
            }
        }
        verifyCodeService.send(mobile,IPUtils.getIpAddr(request));
        if(time ==0){
            time = date;
        }
        count = count+1;
        max=count+"-"+time;
        verifyCodeService.sentMax(mobile,max);
        Map<String,Object> map = new HashMap<>(1);
        map.put("result",true);
        return Result.success(map);
    }

    /**
     * 上传公用接口
     */
    @PostMapping("/upload/{catalog}")
    @CustomLog
    @IsLogin
    public Result upload(@PathVariable("catalog")String catalog,@RequestParam("file") MultipartFile file,@RequestHeader("appType")String appType){
        boolean flag = false;
        Map<String,Object> map = new HashMap<>(1);
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        String path ="/images"+"/"+catalog+"/"+ UUID.randomUUID() + "." + FilenameUtils.getExtension(fileName);
        try {
            flag = ftpPlugin.upload(path,file.getInputStream(),contentType,appType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!flag){
            return Result.fail(ErrorCode.C3004);
        }
        map.put("result",path);
        return Result.success(map);

    }

    @PostMapping("/config/{module}")
    @IsLogin
    @CustomLog
    public Result getAppConfig(@PathVariable("module")String module,@RequestHeader("appType")String appType){
        Map<String,String> map = commonService.getAppConfig(module,appType);
        return Result.success(map);
    }

}
