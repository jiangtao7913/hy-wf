package com.hy.wf.admin.modules.controller;

import com.hy.wf.admin.common.annotation.SysLog;
import com.hy.wf.admin.common.utils.FtpPlugin;
import com.hy.wf.admin.modules.service.CommonService;
import com.hy.wf.admin.modules.service.FunctionUseService;
import com.hy.wf.admin.modules.service.InstallService;
import com.hy.wf.admin.modules.service.MoneyRecordService;
import com.hy.wf.admin.modules.sys.controller.AbstractController;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.common.util.R;
import com.hy.wf.common.validator.ValidatorUtils;
import com.hy.wf.common.validator.group.AddGroup;
import com.hy.wf.entity.Ad;
import com.hy.wf.entity.AppConfig;
import com.hy.wf.entity.Function;
import org.apache.commons.io.FilenameUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-21 15:35
 **/
@RestController
@RequestMapping("/common")
public class CommonController extends AbstractController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private FtpPlugin ftpPlugin;
    @Autowired
    private MoneyRecordService moneyRecordService;
    @Autowired
    private InstallService installService;
    @Autowired
    private FunctionUseService functionUseService;

    /**
     * 查看功能列表
     */
    @RequestMapping("/function/list")
    @RequiresPermissions("function:list")
    public R functionList(@RequestParam Map<String, Object> params){
        PageUtils page = commonService.queryFunctionPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 保存功能
     */
    @SysLog("保存功能")
    @RequestMapping("/function/save")
    @RequiresPermissions("function:save")
    public R functionSave(@RequestBody Function function){
        ValidatorUtils.validateEntity(function, AddGroup.class);
        commonService.saveFunction(function);
        return R.ok();
    }

    /**
     * 修改功能
     */
    @SysLog("保存功能")
    @RequestMapping("/function/update")
    @RequiresPermissions("function:update")
    public R functionUpdate(@RequestBody Function function){
        ValidatorUtils.validateEntity(function, AddGroup.class);
        commonService.updateFunction(function);
        return R.ok();
    }

    /**
     * 功能信息
     */
    @RequestMapping("/function/info/{functionId}")
    @RequiresPermissions("function:info")
    public R functionInfo(@PathVariable("functionId") Long functionId){
        Function function = commonService.selectFunctionById(functionId);
        return R.ok().put("function", function);
    }

    /**
     * 删除功能
     *
     */
    @SysLog("删除功能")
    @RequestMapping("/function/delete")
    @RequiresPermissions("function:delete")
    public R deleteFunction(@RequestBody Long[] functionIds){
        commonService.deleteFunction(functionIds, commonService.selectFunctionById(functionIds[0]));
        return R.ok();
    }

    /**
     * 查看功能列表
     */
    @RequestMapping("/ad/list")
    @RequiresPermissions("ad:list")
    public R adList(@RequestParam Map<String, Object> params){
        PageUtils page = commonService.queryAdPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 保存广告
     */
    @SysLog("保存广告")
    @RequestMapping("/ad/save")
    @RequiresPermissions("ad:save")
    public R adSave(@RequestBody Ad ad){
        ValidatorUtils.validateEntity(ad, AddGroup.class);
        commonService.saveAd(ad);
        return R.ok();
    }

    /**
     * 修改广告
     */
    @SysLog("修改广告")
    @RequestMapping("/ad/update")
    @RequiresPermissions("ad:update")
    public R functionUpdate(@RequestBody Ad ad){
        ValidatorUtils.validateEntity(ad, AddGroup.class);
        commonService.updateAd(ad);
        return R.ok();
    }

    /**
     * 广告信息
     */
    @RequestMapping("/ad/info/{id}")
    @RequiresPermissions("ad:info")
    public R adInfo(@PathVariable("id") Long id){
        Ad ad = commonService.selectAdById(id);
        return R.ok().put("ad", ad);
    }

    /**
     * 删除广告
     *
     */
    @SysLog("删除广告")
    @RequestMapping("/ad/delete")
    @RequiresPermissions("ad:delete")
    public R deleteAd(@RequestBody Long[] ids){
        commonService.deleteAd(ids, commonService.selectAdById(ids[0]));
        return R.ok();
    }


    /**
     * 查看app日志列表
     */
    @RequestMapping("/appLog/list")
    @RequiresPermissions("log:list")
    public R appLogList(@RequestParam Map<String, Object> params){
        PageUtils page = commonService.queryAppLogPage(params);
        return R.ok().put("page", page);
    }

    @SysLog("删除日志")
    @RequestMapping("/log/delete")
    @RequiresPermissions("log:delete")
    public R deleteLog(@RequestBody Long[] ids){
        commonService.deleteLog(ids);
        return R.ok();
    }

    /**
     * 查看字典列表
     */
    @RequestMapping("/config/list")
    @RequiresPermissions("config:list")
    public R configList(@RequestParam Map<String, Object> params){
        PageUtils page = commonService.queryConfigPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 保存字典
     */
    @SysLog("保存字典")
    @RequestMapping("/config/save")
    @RequiresPermissions("config:save")
    public R configSave(@RequestBody AppConfig appConfig){
        ValidatorUtils.validateEntity(appConfig, AddGroup.class);
        if(appConfig.getModel().equals("version")){
            commonService.saveConfig(appConfig);
        }else{
            commonService.saveConfigOther(appConfig);
        }

        return R.ok();
    }

    /**
     * 修改字典
     */
    @SysLog("修改字典")
    @RequestMapping("/config/update")
    @RequiresPermissions("config:update")
    public R configUpdate(@RequestBody AppConfig appConfig){
        ValidatorUtils.validateEntity(appConfig, AddGroup.class);
        if(appConfig.getModel().equals("version")){
            commonService.updateConfig(appConfig);
        }else{
            commonService.updateConfigOther(appConfig);
        }
        return R.ok();
    }

    /**
     * 字典信息
     */
    @RequestMapping("/config/info/{id}")
    @RequiresPermissions("config:info")
    public R configInfo(@PathVariable("id") Long id){
        AppConfig appConfig = commonService.selectConfigById(id);
        return R.ok().put("config", appConfig);
    }

    @SysLog("删除字典")
    @RequestMapping("/config/delete")
    @RequiresPermissions("config:delete")
    public R deleteConfig(@RequestBody Long[] ids){
        commonService.deleteConfig(ids, commonService.selectConfigById(ids[0]));
        return R.ok();
    }

    /**
     * 上传公用接口
     */
    @PostMapping("/upload/{catalog}")
    public R upload(@PathVariable("catalog")String catalog, @RequestParam("file") MultipartFile file){
        boolean flag = false;
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        String path ="/images"+"/"+catalog+"/"+ UUID.randomUUID() + "." + FilenameUtils.getExtension(fileName);
        try {
            flag = ftpPlugin.upload(path,file.getInputStream(),contentType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!flag){
            return R.error("上传失败");
        }
        return R.ok().put("url",path);
    }

    /**
     * 查看安裝列表
     */
    @RequestMapping("/install/list")
    @RequiresPermissions("install:list")
    public R installList(@RequestParam Map<String, Object> params){
        PageUtils page = installService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 查看功能使用列表
     */
    @RequestMapping("/functionUse/list")
    @RequiresPermissions("functionUse:list")
    public R functionUseList(@RequestParam Map<String, Object> params){
        PageUtils page = functionUseService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     *  发送提现短信消息(每天9点半发送)
     */
    @Scheduled(cron ="0 30 9 * * ?")
    public void sendWithdrawMsg(){
        moneyRecordService.sendWithdrawMsg();
    }

}
