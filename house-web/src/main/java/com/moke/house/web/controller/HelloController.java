package com.moke.house.web.controller;

import com.moke.house.biz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {
    @Autowired
    private UserService userService;
    @RequestMapping("/hello")
    public String hello(ModelMap modelMap){
        modelMap.addAttribute(userService.getUsers().get(0));
        return "hello";
    }
    @RequestMapping("/index")
    public String index(ModelMap modelMap){
        //modelMap.addAttribute(userService.getUsers().get(0));
        return "homepage/index";
    }
}
