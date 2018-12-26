package com.dity.ssm.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author:yuhang
 * @Date:2018/12/26
 */
@Aspect
@Component
public class LogAspect {

    /**
     * 定义切入点表达式
     */
    @Pointcut("execution(* com.dity.ssm.controller.*.*(..))")
    public void jp(){}

    @Before("jp()")
    public void before(){
        System.out.println("前置切面成功了...");
    }

}
