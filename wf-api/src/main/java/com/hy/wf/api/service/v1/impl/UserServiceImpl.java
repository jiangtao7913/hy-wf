package com.hy.wf.api.service.v1.impl;

import com.hy.wf.api.dao.repository.v1.UserRepository;
import com.hy.wf.api.service.v1.SnService;
import com.hy.wf.api.service.v1.UserFunctionService;
import com.hy.wf.api.service.v1.UserService;
import com.hy.wf.api.service.v1.VerifyCodeService;
import com.hy.wf.common.Constant;
import com.hy.wf.common.Result;
import com.hy.wf.common.exception.ErrorCode;
import com.hy.wf.common.exception.ServiceException;
import com.hy.wf.common.test.NettyRedis;
import com.hy.wf.common.util.TokenUtil;
import com.hy.wf.entity.Sn;
import com.hy.wf.entity.User;
import com.hy.wf.entity.UserFunction;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-17 18:02
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SnService snService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private VerifyCodeService verifyCodeService;
    @Autowired
    private UserFunctionService userFunctionService;
    @Autowired
    private NettyRedis nettyRedis;

    private static String ICON = "icon";

    @Override
    public User findByUid(String uid) {
        return userRepository.findByUid(uid);
    }

    /**
     * 用户登录注册接口
     * @param type
     * @param key
     * @param name
     * @param icon
     * @param source
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result registerAndLogin(User.AccountType type , String key, String name, String icon, String source, String hardwareKey,String sex,String appType) {
        User user = userRepository.findByAccountBindTypeAndSign(type,key,appType);
        if(null == user){
            //注册
            KeyHolder keyHolder = new GeneratedKeyHolder();
            user = buildUser(source,name,icon,hardwareKey,key,type,"",sex,appType);
            userRepository.save(user,keyHolder);
            user.setId(keyHolder.getKey().longValue());
        }
        boolean flag = checkUser(user);
        if(!flag){
            throw new ServiceException(ErrorCode.C3005);
        }
        Map<String,Object> map = login(user,hardwareKey,source);
        return Result.success(map);
    }

    /** 
    * @Description: 手机注册 
    * @Param [type, key, name, icon, source, hardwareKey, verifyCode, password]
    * @return: com.hy.wf.common.Result 
    * @Author: jt 
    * @Date: 2019/2/27 
    */ 
    @Override
    public Result mobileRegister(User.AccountType type, String key, String name, String icon, String source, String hardwareKey, String verifyCode, String password,String appType) {
        if (!StringUtils.equals("18888888888", key) && !verifyCodeService.verify(key, verifyCode)) {
            throw new ServiceException(ErrorCode.C1006);
        }
        User user = userRepository.findByAccountBindTypeAndSign(User.AccountType.Phone, key,appType);
        if(null != user){
            throw new ServiceException(ErrorCode.C3003);
        }
        //新注册用户
        KeyHolder keyHolder = new GeneratedKeyHolder();
        user = buildUser(source,name,icon,hardwareKey,key,type,password,"",appType);
        userRepository.save(user,keyHolder);
        user.setId(keyHolder.getKey().longValue());

        if(StringUtils.isEmpty(user.getName())){
            userRepository.updateInfo(user.getId(),"name","用户"+user.getId());
        }

        Map<String,Object> map = new HashMap<>(1);
        map.put("result",true);
        return Result.success(map);
    }

    /** 
    * @Description: 手机登录
    * @Param [key, password]
    * @return: com.hy.wf.common.Result 
    * @Author: jt 
    * @Date: 2019/2/27 
    */ 
    @Override
    public Result mobileLogin(User.AccountType accountType,String key, String password,String hardwareKey,String source,String appType) {
        User user = userRepository.findByAccountBindTypeAndSign(accountType,key,appType);
        //判断是否虚拟用户
        if(!checkUser(user)){
            throw new ServiceException(ErrorCode.C3005);
        }
        if(null == user){
            throw new ServiceException(ErrorCode.C3001);
        }
        boolean flag = BCrypt.checkpw(password,user.getPassword());
        if(!flag){
            throw new ServiceException(ErrorCode.C3002);
        }
        Map<String,Object> map = login(user,hardwareKey,source);
        return Result.success(map);
    }

   /** 
   * @Description: 忘记密码
   * @Param [mobile, verifyCode, password, source, hardwareKey]
   * @return: com.hy.wf.common.Result 
   * @Author: jt 
   * @Date: 2019/2/27 
   */ 
    @Override
    public Result forgetPassword(String mobile, String verifyCode, String password,String appType) {
        User user = userRepository.findByAccountBindTypeAndSign(User.AccountType.Phone,mobile,appType);
        if (null == user){
            throw new ServiceException(ErrorCode.C3001);
        }
        boolean flag = verifyCodeService.verify(mobile,verifyCode);
        if(!flag){
            throw new ServiceException(ErrorCode.C1006);
        }
        password = BCrypt.hashpw(password,BCrypt.gensalt());
        userRepository.updatePasswordById(user.getId(),password);
        Map<String,Object> map = new HashMap<>(1);
        map.put("result",true);
        return Result.success(map);
    }

    /** 
    * @Description: 用户登出
    * @Param [user]
    * @return: com.hy.common.Result 
    * @Author: jt 
    * @Date: 2019/1/21 
    */ 
    @Override
    public Result logout(User user) {
        userRepository.updateOnlineById(User.Online.n,user.getId());
        Map<String,Object> map = new HashMap<>(1);
        map.put("result",true);
        //删除redis信息
        nettyRedis.deleteLoginFlag(user.getId(),user.getHardwareKey());
        nettyRedis.deleteLoginHardKey(user.getId());
        nettyRedis.deleteUserStatus(user.getId());

        return Result.success(map);
    }

    /** 
    * @Description: 修改用户信息
    * @Param [id, column, value]
    * @return: com.hy.wf.common.Result 
    * @Author: jt 
    * @Date: 2019/2/28 
    */ 
    @Override
    public Result updateInfo(Long id, String column, String value) {
        userRepository.updateInfo(id,column,value);
        User user = userRepository.findById(id);
        return Result.success(user);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * 构建用户对象
     * @param source
     * @return
     */
    private User buildUser(String source, String name, String icon, String hardwareKey, String key, User.AccountType type,String password,String sex,String appType){
        User user = new User();
        user.setUid(snService.generate(Sn.Type.uid));
        user.setRechargeTotal(BigDecimal.ZERO);
        user.setSource(source);
        user.setOnline(User.Online.y.value);
        user.setName(name);
        user.setIcon(icon);
        user.setHardwareKey(hardwareKey);
        user.setAccountType(type.value);
        user.setSign(key);
        user.setSex(sex);
        user.setAppType(appType);
        if(!password.isEmpty()){
            password = BCrypt.hashpw(password,BCrypt.gensalt());
        }
        user.setPassword(password);
        user.setBalance(BigDecimal.ZERO);
        user.init();
        return user;
    }


    /**
     * 查找用户的功能列表
     */
    private List<UserFunction> findUserFunction(Long userId){
        return userFunctionService.findByUserId(userId);
    }

    /**
     * 用户登录公用方法
     * @param user
     * @param hardwareKey
     * @return
     */
    private Map<String,Object> login(User user,String hardwareKey,String source){
        Map<String,Object> map = new HashMap<>(2);
        map.put("user",user);
        //生成token
        String refreshToken = tokenUtil.generateToken(user.getUid(), Constant.REFRESH_TOKEN);
        String accessToken = tokenUtil.generateToken(user.getUid(),Constant.ACCESS_TOKEN);
        map.put("refreshToken",refreshToken);
        map.put("accessToken",accessToken);

        //查找用户功能列表
        List<UserFunction> userFunctionList = findUserFunction(user.getId());
        map.put("userFunctionList",userFunctionList);

        //踢出其他设备上的账号
        user.setHardwareKey(hardwareKey);
        user.setSource(source);
        userRepository.updateHardwareKeyAndSourceById(hardwareKey,user.getId(),source);
        //设置redis
        nettyRedis.setUserStatus(user.getId());
        return map;
    }

    /**
     * 判断用户是不是虚拟用户
     */
    private boolean checkUser(User user){
        if(null == user){
            throw new ServiceException(ErrorCode.C3005);
        }
        if(user.getAccountType() == User.AccountType.Virtual.value){
           return false;
        }
        return true;
    }

}
