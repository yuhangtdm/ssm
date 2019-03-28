package com.dity.ssm.mapper;

import com.dity.ssm.entity.User;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
 * @author:yuhang
 * @Date:2018/12/25
 */
public interface UserMapper {

    // 查询所有用户
    List<User> getAll();

    /**
     * 可以返回一个map 指定key 值是查出的值
     */
    @MapKey("id")
    Map<Long,User> getMap();
}
