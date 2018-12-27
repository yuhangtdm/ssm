package com.dity.ssm.aop;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author:yuhang
 * @Date:2018/12/27
 */
public class UserBeforeAdvice  implements MethodBeforeAdvice{
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("编程式前置通知");
    }
}
