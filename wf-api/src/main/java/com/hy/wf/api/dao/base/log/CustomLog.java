package com.hy.wf.api.dao.base.log;

import java.lang.annotation.*;

/**
 * @program: hy-wf
 * @description: 自定义日志记录注解
 * @author: jt
 * @create: 2019-01-09 17:57
 **/
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CustomLog {
    String action() default "";
}
