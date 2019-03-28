package com.dity.ssm.service.impl;

import com.dity.ssm.aop.UserBeforeAdvice;
import com.dity.ssm.entity.User;
import com.dity.ssm.mapper.UserMapper;
import com.dity.ssm.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

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

    @Autowired
    private UserMapper userMapper;

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

    @Test
    public void getMap(){
        Map<Long, User> map = userMapper.getMap();
        System.out.println(map);


    }

}