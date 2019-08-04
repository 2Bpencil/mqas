package com.tyf.mqas.code.controller;



import com.tyf.mqas.code.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;
    @RequestMapping(value = "userManage",method = RequestMethod.GET)
    public String userManage(){
        return "system/user";
    }



}
