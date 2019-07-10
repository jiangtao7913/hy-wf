package com.hy.wf.common.exception;

import com.hy.wf.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * @program: hy-wf
 * @description:统一异常处理类
 * @author: jt
 * @create: 2019-01-04 16:22
 **/
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result signException(HttpServletRequest request, Exception e) {
       e.printStackTrace();
       Map<String,Object> map = new HashMap(2);
       if(e instanceof ServiceException){
           return Result.fail(((ServiceException) e).getErrorCode());
       }else if(e instanceof HttpRequestMethodNotSupportedException){
           ServiceException e1 = new ServiceException(ErrorCode.C401);
           return Result.fail(e1.getErrorCode());
       }else if (e instanceof TypeMismatchException){
           ServiceException e1 = new ServiceException(ErrorCode.C400);
           return Result.fail(e1.getErrorCode());
       }else if (e instanceof BindException){
           BindingResult result =  ((BindException) e).getBindingResult();
           if (result.hasErrors()) {
               ServiceException e1 = new ServiceException(ErrorCode.C400);
               return Result.fail(e1.getErrorCode());
           }
           return Result.fail();
       } else{
           ServiceException e1 = new ServiceException(ErrorCode.C500);
           return Result.fail(e1.getErrorCode());
        }
    }

}