package com.moke.house.web.controller;

import com.moke.house.biz.service.UserService;
import com.moke.house.common.constants.CommonConstants;
import com.moke.house.common.model.User;
import com.moke.house.common.result.ResultMsg;
import com.moke.house.common.utils.HashUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping("accounts/register")
    public String accountRegister(User account, ModelMap modelmap){
        if(account==null || account.getName()==null)
            return "/user/accounts/register";
        ResultMsg resultMsg =  UserHelper.validate(account);
        if(resultMsg.isSuccess() && userService.addAccount(account))
            return "/user/accounts/registerSubmit";
        else
            return "redirect:/accounts/register?"+resultMsg.asUrlParams();
    }
    @RequestMapping("accounts/verify")
    public String verifyUser(String key){
        boolean result = userService.enable(key);
        if(result){
            return "redirect:/index?"+ResultMsg.successMsg("激活成功").asUrlParams();
        }
        else
            return "redirect:/accounts/register?"+ResultMsg.successMsg("激活失败,请确认链接是否过期").asUrlParams();
    }

    //********************login*************************
    @RequestMapping("/accounts/signin")
    public String signin(HttpServletRequest request){
        String name =  request.getParameter("username");
        String pass =  request.getParameter("password");
        String target =  request.getParameter("target");
        if(name==null || pass ==null)
        {
            request.setAttribute("target",target);
            return "/user/accounts/signin";
        }
        User user = userService.auth(name,pass);
        if(user==null)
            return "redirect:/accounts/signin"
                    +"target="+target
                    +"&username="+name
                    +"&"+ResultMsg.errorMsg("用户名或密码错误").asUrlParams();
        else{
            HttpSession session =  request.getSession();
            session.setAttribute(CommonConstants.USER_ATTRIBUTE,user);
            session.setAttribute(CommonConstants.PLAIN_USER_ATTRIBUTE,user);
            return StringUtils.isNoneBlank(target)?"redirect:"+target:"redirect:/index";
        }
    }
    @RequestMapping("/accounts/logout")
    public String logout(HttpServletRequest request){
        HttpSession httpSession = request.getSession(true);
        httpSession.invalidate();
        return "redirect:/index";
    }
    //***********************profile************************
    @RequestMapping("/accounts/profile")
    public String profile(HttpServletRequest request,User updateUser,ModelMap modelMap){
        if(updateUser.getEmail()==null)
            return "/user/accounts/profile";
        userService.updateUser(updateUser,updateUser.getEmail());
        User user  = new User();
        user.setEmail(updateUser.getEmail());
        List<User> list  =userService.getUserByQuery(user);
        request.getSession(true).setAttribute(CommonConstants.PLAIN_USER_ATTRIBUTE,list.get(0));
        return "redirect:/accounts/profile?"+ResultMsg.successMsg("更新成功").asUrlParams();
    }

    /**
     * 修改密码操作
     *
     * @param email
     * @param password
     * @param newPassword
     * @param confirmPassword
     * @param mode
     * @return
     */
    @RequestMapping("accounts/changePassword")
    public String changePassword(String email, String password, String newPassword,
                                 String confirmPassword, ModelMap mode) {
        User user = userService.auth(email, password);
        if (user == null || !confirmPassword.equals(newPassword)) {
            return "redirct:/accounts/profile?" + ResultMsg.errorMsg("密码错误").asUrlParams();
        }
        User updateUser = new User();
        updateUser.setPasswd(HashUtils.encryPassword(newPassword));
        userService.updateUser(updateUser, email);
        return "redirect:/accounts/profile?" + ResultMsg.successMsg("更新成功").asUrlParams();
    }
}
