package com.dity.ssm.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author:yuhang
 * @Date:2018/12/26
 */
//@Aspect
@Component
public class TimeAspect {

    private Logger logger = LoggerFactory.getLogger(TimeAspect.class);

    /**
     * 定义切入点表达式
     */
    @Pointcut("execution(* com.dity.ssm.service.impl.*.*(..))")
    public void jp(){}

    @Around("jp()")
    public void around(ProceedingJoinPoint joinPoint){
        logger.info("时间切面前置");
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.info("时间切面异常");
        }finally {
            logger.info("时间切面后置");
        }
        logger.info("时间切面返回");

    }
}
