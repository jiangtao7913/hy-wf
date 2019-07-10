package com.hy.wf.common.exception;

import lombok.Data;

/**
 * @program: hy-wf
 * @description: 自定义业务异常
 * @author: jt
 * @create: 2019-01-04 16:22
 **/
@Data
public class ServiceException extends RuntimeException{

    private static final long serialVersionUID = 1291202904744808718L;

    private ErrorCode errorCode;

    public ServiceException(String s, String message) {
        super();
    }

    public ServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(ErrorCode errorCode, String message) {
        errorCode.setMessage(message);
        this.errorCode = errorCode;
    }

}
