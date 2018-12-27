package com.dity.ssm.service.impl;

import com.dity.ssm.aop.UserBeforeAdvice;
import com.dity.ssm.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author:yuhang
 * @Date:2018/12/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-applicationContext.xml")
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    public void getAll() throws Exception {
        userService.getAll();
    }

    @Test
    public void getUserName() throws Exception {
        String userName = userService.getUserName();
    }

    @Test
    public void save() throws Exception {
        userService.save("张三");
    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void codeAop(){
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(new UserServiceImpl());
        proxyFactory.addAdvice(new UserBeforeAdvice());
        UserService userService = (UserService) proxyFactory.getProxy();
        userService.getUserName();
    }

}