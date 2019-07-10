package com.hy.wf.common.exception;

import com.hy.wf.common.Result;
import com.hy.wf.common.annotation.IsLogin;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: hy-wf
 * @description: 捕捉404异常
 * @author: jt
 * @create: 2019-01-07 10:13
 **/
@Controller
public class NotFoundException implements ErrorController {

    @Override
    @IsLogin
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = {"/error"})
    @ResponseBody
    public Result error() {
        return Result.fail(ErrorCode.C404);
    }

}
