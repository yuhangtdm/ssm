package com.dity.ssm.config;


import com.dity.ssm.entity.User;

import java.beans.PropertyEditorSupport;

/**
 * 自定义属性编辑器
 * @author:yuhang
 * @Date:2018/12/27
 */
public class CustomDeptEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        User user = new User();
        String[] array = text.split("-");
        user.setId(Integer.parseInt(array[0]));
        user.setUsername(array[1]);
        setValue(user);
    }
}
