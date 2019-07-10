package com.hy.wf.api.config;

import com.alibaba.fastjson.JSON;
import com.hy.wf.api.service.v1.UserService;
import com.hy.wf.common.Constant;
import com.hy.wf.common.Result;
import com.hy.wf.common.annotation.IsLogin;
import com.hy.wf.common.exception.ErrorCode;
import com.hy.wf.common.util.TokenUtil;
import com.hy.wf.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @program: hy-wf
 * @description: 拦截器
 * @author: jt
 * @create: 2019-01-04 15:16
 **/
@Component
@Slf4j
public class InterceptorHandler extends HandlerInterceptorAdapter {

    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private UserService userService;

    public static final String UID = "UID";
    public static final String USER = "USER";

    public static final String REFRESHTOKEN = "refreshToken";

    /**
     * @Description: 控制器执行前调用, 预处理可以进行编码，安全等控制
     * @Param: [request, response, handler]
     * @return: boolean
     * @Author: jt
     * @Date: 2018/12/25
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            IsLogin annotation = ((HandlerMethod) handler).getMethodAnnotation(IsLogin.class);
            if (null != annotation) {
                if (!annotation.value()) {
                    log.warn("no auth ", handler);
                }
                return true;
            }
        } else {
            return true;
        }

        Result respStatusCode = verify(request, response);
        if (respStatusCode != null) {
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            byte[] jsonData = JSON.toJSONBytes(respStatusCode);
            try (OutputStream os = response.getOutputStream()) {
                os.write(jsonData);
            }
            return false;
        }
        return true;
    }

    private Result verify(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader("refreshToken");
        String accessToken = request.getHeader("accessToken");
        //校验token
        Result result = checkToken(refreshToken, accessToken, response);
        if (result.getSuccess() == false) {
            return result;
        }
        //检验uid是否存在
        String uid = result.getData().toString();
        User user = userService.findByUid(uid);
        if (user == null) {
            return Result.fail(ErrorCode.C1001);
        }
        request.setAttribute(UID, uid);
        request.setAttribute(USER, user);
        return null;
    }

    /**
     * 校验token
     */
    public Result checkToken(String refreshToken, String accessToken, HttpServletResponse response) {
        if (StringUtils.isEmpty(refreshToken)) {
            log.warn("refreshToken --- 为空");
            return Result.fail(ErrorCode.C1000);
        }
        String uid = tokenUtil.pareToken(refreshToken);
        if (StringUtils.isEmpty(uid)) {
            return checkAccessToken(accessToken, response);
        }
        return Result.success(uid);
    }

    /**
     * 校验accessToken
     */
    public Result checkAccessToken(String accessToken, HttpServletResponse response) {
        if (StringUtils.isEmpty(accessToken)) {
            log.warn("accessToken --- 为空");
            return Result.fail(ErrorCode.C1000);
        }
        String uid = tokenUtil.pareToken(accessToken);
        if (StringUtils.isEmpty(uid)) {
            log.warn("accessToken --- 解析为空");
            return Result.fail(ErrorCode.C1000);
        }
        setResponseHeader(response, uid);
        return Result.success(uid);
    }

    /**
     * token续签
     */
    private void setResponseHeader(HttpServletResponse response, String uid) {
        //生成token
        String refreshToken = tokenUtil.generateToken(uid, Constant.REFRESH_TOKEN);
        String accessToken = tokenUtil.generateToken(uid, Constant.ACCESS_TOKEN);
        response.setHeader("refreshToken", refreshToken);
        response.setHeader("accessToken", accessToken);
    }

    /**
     * @Description: 生成视图之前执行
     * @Param: [request, response, handler, modelAndView]
     * @return: void
     * @Author: jt
     * @Date: 2018/12/25 +
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * @Description: 在DispatcherServlet完全处理完请求后被调用，可用于清理资源等。返回处理（已经渲染了页面），
     * 可以根据ex是否为null判断是否发生了异常，进行日志记录；
     * @Param: [request, response, handler, ex]
     * @return: void
     * @Author: jt
     * @Date: 2018/12/25
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }
}
