package com.dity.ssm.controller;

import com.dity.ssm.config.CustomDeptEditor;
import com.dity.ssm.entity.User;
import com.dity.ssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author:yuhang
 * @Date:2018/12/25
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "getAll",produces = {"application/json;charset=UTF-8"})
    public List<User> getAll(){
        return userService.getAll();
    }

    @ResponseBody
    @RequestMapping(value = "getUser")
    public User getUser(User user){
       return user;
    }

    @ResponseBody
    @RequestMapping(value = "save")
    public User save(User user){
        System.out.println(user);
        return user;
    }

    @InitBinder
    public void register(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(User.class,new CustomDeptEditor());

    }
}
