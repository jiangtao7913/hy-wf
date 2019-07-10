package com.hy.wf.api.web;

import com.hy.wf.entity.User;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: hy-wf
 * @description: 控制层基类
 * @author: jt
 * @create: 2019-01-04 18:06
 **/
public class BaseController {

    @Autowired
    @Getter
    protected HttpServletRequest request;

    /** 
    * @Description: 获取用户uid
    * @Param: [] 
    * @return: java.lang.String 
    * @Author: jt 
    * @Date: 2019/1/4 
    */ 
    public String getUid(){
        String uid = (String) request.getAttribute("UID");
        return uid;
    }

    public User getCurrentUser(){
        User user = (User) request.getAttribute("USER");
        return user;
    }



}
