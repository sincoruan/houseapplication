package com.moke.house.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);
    @ExceptionHandler(value={Exception.class,NullPointerException.class})
    public String error500(HttpServletRequest httpServletRequest,Exception e){
        logger.info(e.getMessage());
        e.printStackTrace();
        logger.info("error url:"+httpServletRequest.getRequestURI());
        return "error/500";
    }
}
