package com.moke.house.web.interceptor;


import com.moke.house.common.constants.CommonConstants;
import com.moke.house.common.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

@Component
public class AuthActionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user  = UserContext.getUser();
        if(user==null){
            String mstring = URLEncoder.encode("UTF-8");
            String target  = URLEncoder.encode(request.getRequestURI(),"utf-8");
            if(request.getMethod().equals("GET"))
                response.sendRedirect("/accounts/signin?errorMsg="+mstring+"&target="+target);
            else
                response.sendRedirect("/accounts/signin?errorMsg="+mstring);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
