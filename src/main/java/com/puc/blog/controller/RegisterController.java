package com.puc.blog.controller;


import com.puc.blog.service.LoginService;
import com.puc.blog.vo.Result;
import com.puc.blog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    //TODO 这里把LoginService换成注册专用的 SSO Service  后期如果把登录注册功能踢出去 可以独立提供接口服务
    public Result register(@RequestBody LoginParam loginParam){

        return loginService.register(loginParam);
    }
}
