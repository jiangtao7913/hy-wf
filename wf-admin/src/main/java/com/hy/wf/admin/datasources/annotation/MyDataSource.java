package com.hy.wf.admin.datasources.annotation;

import java.lang.annotation.*;

/**
 * @program: hy-wf
 * @description:多数据源注解
 * @author: jt
 * @create: 2019-01-23 16:24
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyDataSource {
    String name() default "";
}
