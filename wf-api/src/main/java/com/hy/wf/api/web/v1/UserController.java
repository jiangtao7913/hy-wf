package com.hy.wf.api.web.v1;

import com.hy.wf.api.dao.base.log.CustomLog;
import com.hy.wf.api.service.v1.UserService;
import com.hy.wf.api.web.BaseController;
import com.hy.wf.common.Result;
import com.hy.wf.common.annotation.IsLogin;
import com.hy.wf.common.exception.ErrorCode;
import com.hy.wf.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.FormParam;


/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-18 14:55
 **/
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 三方用户注册登录
     */
    @PostMapping("/{type}/{key}")
    @IsLogin
    @CustomLog
    public Result registerAndLogin(@PathVariable("key")String key, @PathVariable("type")String type,
                                   @FormParam("name")String name, @FormParam("icon")String icon,@FormParam("sex")String sex,
                                   @RequestHeader("source")String source, @RequestHeader("hardwareKey")String hardwareKey,
                                   @RequestHeader("appType")String appType){
        User.AccountType mType = User.AccountType.valueOf(type);
        if(null == mType){
            return Result.fail(ErrorCode.C400);
        }
        return userService.registerAndLogin(mType,key,name,icon,source,hardwareKey,sex,appType);
    }

    /**
     * 手机号注册
     * @return
     */
    @PostMapping("/mobile/register")
    @IsLogin
    @CustomLog
    public Result mobileRegister(@FormParam("mobile")String mobile, @FormParam("type")String type,
                                 @FormParam("verifyCode")String verifyCode, @FormParam("password")String password,
                                 @RequestHeader("source")String source, @RequestHeader("hardwareKey")String hardwareKey,
                                 @RequestHeader("appType")String appType){
        User.AccountType mType = User.AccountType.valueOf(type);
        if(null == mType){
            return Result.fail(ErrorCode.C400);
        }
        return userService.mobileRegister(mType,mobile,"","",source,hardwareKey,verifyCode,password,appType);
    }

    /**
     * 手机号登录
     * @return
     */
    @PostMapping("/mobile/login")
    @IsLogin
    @CustomLog
    public Result mobileLogin(@FormParam("mobile")String mobile, @FormParam("type")String type,
                              @FormParam("password")String password,
                              @RequestHeader("source")String source, @RequestHeader("hardwareKey")String hardwareKey,
                              @RequestHeader("appType")String appType){
        User.AccountType mType = User.AccountType.valueOf(type);
        if(null == mType){
            return Result.fail(ErrorCode.C400);
        }
        return userService.mobileLogin(mType,mobile,password,hardwareKey,source,appType);
    }

    /**
     * 找回密码
     * @return
     */
    @PostMapping("/forgetPassword")
    @IsLogin
    @CustomLog
    public Result forgetPassword(@FormParam("mobile")String mobile,@FormParam("verifyCode")String verifyCode,
                                 @FormParam("password")String password,
                                 @RequestHeader("appType")String appType){
       return userService.forgetPassword(mobile,verifyCode,password,appType);
    }

    /**
     * 登出
     * @return
     */
    @PostMapping("/logout")
    @CustomLog
    public Result logout(){
      return userService.logout(getCurrentUser());
    }

    /**
     * 修改用户个人信息
     */
    @PostMapping("/update/{column}")
    @CustomLog
    public Result updateInfo(@PathVariable("column")String column,@FormParam("value")String value){
        return userService.updateInfo(getCurrentUser().getId(),column,value);
    }

}
