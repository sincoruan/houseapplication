package com.moke.house.web;

import com.moke.house.biz.service.UserService;
import com.moke.house.common.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthTest {
    @Autowired
    UserService userService;
    @Test
    public void testAuth(){
        User user  =userService.auth("Xingke Ruan","ruanxk");
        assert user!=null;
        System.out.println(user.getAboutme());
    }
}
