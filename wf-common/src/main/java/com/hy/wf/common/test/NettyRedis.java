package com.hy.wf.common.test;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-04-17 17:32
 **/
@Component
public class NettyRedis {
    /**
     * 从redis 获取是否登录标志
     * @return
     */
    @CacheEvict(value = "netty",key = "#userId+'.hardwareKey'")
    public void deleteLoginHardKey(Long userId){

    }

    /**
     * 从redis 获取是否登录标志
     * @return
     */
    @CachePut(value = "netty",key = "#userId+'.hardwareKey'")
    public String setLoginHardKey(Long userId,String hardwareKey){
        return hardwareKey;
    }

    /**
     * 从redis 获取是否登录标志
     * @return
     */
    @Cacheable(value = "netty",key = "#userId+'.hardwareKey'")
    public String getLoginHardKey(Long userId){
        return "null";
    }

    @CacheEvict(value = "netty",key = "#userId+'.'+#hardwareKey")
    public void deleteLoginFlag(Long userId,String hardwareKey){

    }

    /**
     * 从redis 获取是否登录标志
     * @return
     */
    @Cacheable(value = "netty",key = "#userId+'.'+#hardwareKey")
    public String getLoginFlag(Long userId,String hardwareKey){
        return "null";
    }

    /**
     * 从redis 获取是否登录标志
     * @return
     */
    @CachePut(value = "netty",key = "#userId+'.'+#hardwareKey")
    public String setLoginFlag(Long userId,String hardwareKey){
        return "true";
    }


    @CacheEvict(value = "netty",key = "#userId+'.status'")
    public void deleteUserStatus(Long userId){

    }

    /**
     * 获取用户是否在线
     */
    @Cacheable(value = "netty",key = "#userId+'.status'")
    public String getUserStatus(Long userId){
        return "n";
    }

    @CachePut(value = "netty",key = "#userId+'.status'")
    public String setUserStatus(Long userId){
        return "y";
    }
}
