package com.dity.ssm.mapper;

import com.dity.ssm.entity.User;

import java.util.List;

/**
 * @author:yuhang
 * @Date:2018/12/25
 */
public interface UserMapper {

    // 查询所有用户
    List<User> getAll();
}
