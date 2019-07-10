package com.hy.wf.admin.datasources.aspect;

import com.hy.wf.admin.datasources.DataSourceNames;
import com.hy.wf.admin.datasources.DynamicDataSource;
import com.hy.wf.admin.datasources.annotation.MyDataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-23 16:24
 **/
@Aspect
@Component
@Slf4j
public class DataSourceAspect implements Ordered {

    /**
     * 定义一个我们自定义的切点
     */
    @Pointcut("@annotation(com.hy.wf.admin.datasources.annotation.MyDataSource)")
    public void dataSourcePointCut() {

    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        MyDataSource ds = method.getAnnotation(MyDataSource.class);
        if(ds == null){
            DynamicDataSource.setDataSource(DataSourceNames.FIRST);
            log.debug("set datasource is " + DataSourceNames.FIRST);
        }else {
            DynamicDataSource.setDataSource(ds.name());
            log.debug("set datasource is " + ds.name());
        }
        try {
            return point.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
            log.debug("clean datasource");
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
