package com.hy.wf.api.dao.base.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hy.wf.api.dao.repository.v1.AppLogRepository;
import com.hy.wf.common.util.IPUtils;
import com.hy.wf.entity.AppLog;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * @program: hy-wf
 * @description: 自定义切面
 * @author: jt
 * @create: 2019-01-09 17:58
 **/
@Aspect
@Component
public class LogAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AppLogRepository sysLogRepository;

    private AppLog appLog;
    /**
     * 开始时间
     */
    private Long startTime;
    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 切点是我们自定义的注解
     */
    @Pointcut("@annotation(com.hy.wf.api.dao.base.log.CustomLog)")
    public void logPointCut(){

    }

    /**
     * 编写前置通知，前置通知实在我们目标方法执行之前执行
     */
    @Before("logPointCut()")
    public void doBefore(){
       initSysLog(request);
    }

    /**
     * 编写返回通知,返回通知在目标方法正常结束后执行，在返回通知中补全日志信息
     */
    @AfterReturning(value = "logPointCut()",returning = "returnValue")
    public void doAfterReturning(Object returnValue){
        endTime = System.currentTimeMillis();
        appLog.setEndTime(new Date(endTime));
        appLog.setResponseTime(endTime-startTime);
        String message = JSON.toJSONString(returnValue,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue);
        appLog.setResponse(message);
        appLog.setType(AppLog.Type.normal.value);
        //保存日志信息
        sysLogRepository.save(appLog);
    }

    /**
     * 编写异常通知,异常通知在目标方法非正常结束，或发生异常或抛出异常时执行
     * 在异常通知中设置异常信息，并且保存
     */
    @AfterThrowing(value = "logPointCut()",throwing = "e")
    public void doAfterThrowing(Throwable e){
        endTime = System.currentTimeMillis();
        appLog.setEndTime(new Date(endTime));
        appLog.setResponseTime(endTime-startTime);
        String stackMessage = JSON.toJSONString(e,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue);
        appLog.setStackMessage(stackMessage);
        appLog.setResponse("");
        appLog.setType(AppLog.Type.error.value);
        //保存日志信息
        sysLogRepository.save(appLog);
    }


    /**
     * 初始化异常信息
     */
    private void initSysLog(HttpServletRequest request){
        String paramData = JSON.toJSONString(request.getParameterMap(),
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue);
        appLog = new AppLog();
        String ip = IPUtils.getIpAddr(request);
        appLog.setClientIp(ip);
        appLog.setUri(request.getRequestURI());
        appLog.setRequireMethod(request.getMethod());
//        try {
//            paramData = URLEncoder.encode(paramData, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        appLog.setParamData(paramData);
        startTime = System.currentTimeMillis();
        appLog.setRequireTime(new Date(startTime));
        appLog.setStackMessage("");
        appLog.setUid(request.getAttribute("UID") !=null?(String)request.getAttribute("UID"):"");
    }

}
