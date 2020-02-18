package com.moke.house.web.controller;

import com.moke.house.biz.service.UserService;
import com.moke.house.common.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    UserService userService;

    @RequestMapping("/testUsers")
    public List<User> getUsers (){
        return userService.getUsers();
    }
    @RequestMapping("/test500/{id}")
    public List<User> test500 (@PathVariable int id) throws Exception {
        if(id==1)
            throw new Exception("500 internal error test");
        else
            return userService.getUsers();
    }
}
