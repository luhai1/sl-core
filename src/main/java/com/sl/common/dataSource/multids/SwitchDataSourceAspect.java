package com.sl.common.dataSource.multids;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class SwitchDataSourceAspect {
    @Pointcut("@annotation(com.sl.common.dataSource.multids.SwitchDataSource)")
    public void switchDataSourcePointCut() {}

    @Before("switchDataSourcePointCut()")
    public void beforeSwitchDataSource(JoinPoint point) {
        Class<?> className = point.getTarget().getClass();
        String methodName = point.getSignature().getName();
        Class[] argClass = ((MethodSignature)point.getSignature()).getParameterTypes();
        String dataSource = "";
        try {
            Method method = className.getMethod(methodName, argClass);
            if (method.isAnnotationPresent((Class)SwitchDataSource.class)) {
                SwitchDataSource annotation = method.getAnnotation(SwitchDataSource.class);
                dataSource = annotation.value();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DataSourceContextHolder.setDataSource(dataSource);
    }


    @After("switchDataSourcePointCut()")
    public void afterSwitchDataSource(JoinPoint point) { DataSourceContextHolder.clearDataSource(); }
}
