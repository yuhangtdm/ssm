package com.dity.ssm.service;

import com.dity.ssm.entity.User;

import java.util.List;

/**
 * @author:yuhang
 * @Date:2018/12/25
 */
public interface UserService {

    //  查询所有用户
    List<User> getAll();
    // 查询用户名称
    String getUserName();
    // 保存
    void save(String name);
    // 更新
    void update(String name,Integer id);
}
