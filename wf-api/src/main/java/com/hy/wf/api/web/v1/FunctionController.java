package com.hy.wf.api.web.v1;

import com.hy.wf.api.dao.base.log.CustomLog;
import com.hy.wf.api.service.v1.FunctionService;
import com.hy.wf.api.web.BaseController;
import com.hy.wf.common.Result;
import com.hy.wf.common.annotation.IsLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: hy-wf
 * @description:app功能列表
 * @author: jt
 * @create: 2019-03-02 15:11
 **/
@RestController
@RequestMapping("/function")
@Slf4j
public class FunctionController extends BaseController {

    @Autowired
    private FunctionService functionService;

    /**
     * 查询app功能列表
     */
    @PostMapping("/getAll/{position}")
    @CustomLog
    @IsLogin
    public Result getAll(@PathVariable("position")String position,@RequestHeader("appType")String appType){
       return Result.success(functionService.getAllAndAppType(position,appType));
    }

    /**
     * 记录用户功能使用记录
     */
    @PostMapping("/{userId}/{functionId}")
    @CustomLog
    @IsLogin
    public Result functionRecord(@RequestHeader("hardwareKey")String hardwareKey, @PathVariable("userId")Long userId, @PathVariable("functionId")Long functionId){
        return functionService.functionRecord(userId,functionId,hardwareKey);
    }

    /**
     * 检测用户功能是否过期接口
     */
    @PostMapping("/{userId}")
    @CustomLog
    @IsLogin
    public Result checkVip(@PathVariable("userId")Long userId){
        return functionService.checkVip(userId);
    }

}
