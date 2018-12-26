package com.dity.ssm.service.impl;

import com.dity.ssm.entity.User;
import com.dity.ssm.mapper.UserMapper;
import com.dity.ssm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:yuhang
 * @Date:2018/12/25
 */
@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getAll() {
        return userMapper.getAll();
    }

    @Override
    public String getUserName() {
        logger.info("查询用户姓名");
        // throw new RuntimeException("出现异常");
         return "zs";
    }

    @Override
    public void save(String name) {
        logger.info("保存用户姓名");
    }

    @Override
    public void update(String name, Integer id) {
        logger.info("更新用户姓名，id:{},name:{}",id,name);
    }
}
