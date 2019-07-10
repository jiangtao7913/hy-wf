package com.hy.wf.api.service.v1;

import com.hy.wf.common.Result;
import com.hy.wf.entity.User;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-17 18:01
 **/

public interface UserService {

    /** 
    * @Description: 根据uid查询用户
    * @Param [uid]
    * @return: com.hy.wf.dao.entity.v1.User
    * @Author: jt 
    * @Date: 2019/1/17 
    */ 
    User findByUid(String uid);

    /** 
    * @Description: 三方登录注册接口
    * @Param [type, key, name, icon]
    * @return: com.hy.wf.dao.entity.v1.User
    * @Author: jt 
    * @Date: 2019/1/18 
    */ 
    Result registerAndLogin(User.AccountType type, String key, String name, String icon, String source, String hardwareKey,String sex,String appType);

    /**
     *手机注册
     */
    Result mobileRegister(User.AccountType type,String key, String name, String icon, String source, String hardwareKey,String verifyCode,String password,String appType);

    /**
     * 手机登录
     * @param key
     * @param password
     * @return
     */
    Result mobileLogin(User.AccountType accountType, String key, String password,String hardwareKey,String source,String appType);

    /**
     *
     * @return
     */
    Result forgetPassword(String mobile,String verifyCode,String password,String appType);


    /** 
    * @Description: 用户登出 
    * @Param [user]
    * @return: com.hy.common.Result 
    * @Author: jt 
    * @Date: 2019/1/21 
    */ 
    Result logout(User user);

    /**
     * 修改用户个人信息
     */
    Result updateInfo(Long id,String column,String value);

    User findById(Long userId);
}
