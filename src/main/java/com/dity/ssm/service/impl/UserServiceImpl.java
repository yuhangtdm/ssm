package com.dity.ssm.service.impl;

import com.dity.ssm.entity.User;
import com.dity.ssm.mapper.UserMapper;
import com.dity.ssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:yuhang
 * @Date:2018/12/25
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getAll() {
        return userMapper.getAll();
    }
}
