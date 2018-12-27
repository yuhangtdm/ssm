package com.dity.ssm.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author:yuhang
 * @Date:2018/12/26
 */
@Aspect
@Component
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 配置了 前置 返回 后置 异常通知
     * 没有异常时
     *   17:58:09.467 [main] INFO com.dity.ssm.config.LogAspect - aop前置通知
         17:58:09.467 [main] INFO com.dity.ssm.service.impl.UserServiceImpl - 查询用户姓名
         17:58:09.467 [main] INFO com.dity.ssm.config.LogAspect - aop后置通知
         17:58:09.468 [main] INFO com.dity.ssm.config.LogAspect - aop返回通知
     * 当出现异常时
     *  17:54:00.224 [main] INFO com.dity.ssm.config.LogAspect - aop前置通知
        17:54:00.224 [main] INFO com.dity.ssm.service.impl.UserServiceImpl - 查询用户姓名
        17:54:00.225 [main] INFO com.dity.ssm.config.LogAspect - aop后置通知
        17:54:00.225 [main] INFO com.dity.ssm.config.LogAspect - aop异常通知

     配置了环绕通知后
     18:10:12.306 [main] INFO com.dity.ssm.config.LogAspect - aop环绕通知前置
     18:10:12.306 [main] INFO com.dity.ssm.config.LogAspect - aop前置通知
     18:10:12.306 [main] INFO com.dity.ssm.service.impl.UserServiceImpl - 查询用户姓名
     18:10:12.306 [main] INFO com.dity.ssm.config.LogAspect - aop环绕通知后置
     18:10:12.306 [main] INFO com.dity.ssm.config.LogAspect - aop环绕通知返回
     18:10:12.306 [main] INFO com.dity.ssm.config.LogAspect - aop后置通知
     18:10:12.307 [main] INFO com.dity.ssm.config.LogAspect - aop返回通知


     当配置了两个切面的环绕通知时

     18:28:48.546 [main] INFO com.dity.ssm.config.LogAspect - aop环绕通知前置
     18:28:48.547 [main] INFO com.dity.ssm.config.TimeAspect - 时间切面前置
     18:28:48.547 [main] INFO com.dity.ssm.service.impl.UserServiceImpl - 查询用户姓名
     18:28:48.547 [main] INFO com.dity.ssm.config.TimeAspect - 时间切面后置
     18:28:48.547 [main] INFO com.dity.ssm.config.TimeAspect - 时间切面返回
     18:28:48.547 [main] INFO com.dity.ssm.config.LogAspect - aop环绕通知后置
     18:28:48.547 [main] INFO com.dity.ssm.config.LogAspect - aop环绕通知返回
     */

    /**
     * 定义切入点表达式
     */
    @Pointcut("execution(* com.dity.ssm.service.UserService.*(..))")
    public void jp(){}

    /**
     * 前置通知 方法执行之前运行
     * 配置了  && args(name)
     * 当方法没有参数时  该前置通知不执行
     */
    @Before("jp()")
    public void before(){
        logger.info("aop前置通知");
    }

    /**
     * 返回通知 方法正常返回后运行
     * ,returning = "result" 对于有返回值的方法可以获取返回值
     */
    @AfterReturning(value = "jp()",returning = "result")
    public void afterReturning(String result){
        logger.info("aop返回通知,"+result);
    }

    /**
     * 后置通知 方法执行后运行 在返回通知之前运行
     */
//    @After("jp()")
    public void after(){
        logger.info("aop后置通知");
    }

    /**
     * 异常通知 方法执行出现异常后运行
     * throwing = "e" 可以获取异常的具体信息
     */
    @AfterThrowing(value = "jp()",throwing = "e")
    public void throwException(Exception e){
        logger.error("aop异常通知,"+e.getMessage(),e);
    }

//    @Around("jp()")
    public void around(ProceedingJoinPoint joinPoint){
        logger.info("aop环绕通知前置.");
        try {
            joinPoint.proceed();
        }catch (Throwable e){
            logger.info("aop环绕通知异常");
        }finally {
            logger.info("aop环绕通知后置");
        }
        logger.info("aop环绕通知返回");

    }

}
