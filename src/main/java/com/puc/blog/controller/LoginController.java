package com.puc.blog.controller;


import com.puc.blog.service.LoginService;
import com.puc.blog.service.SysUserService;
import com.puc.blog.vo.Result;
import com.puc.blog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController的作用相当于Controller加ResponseBody共同作用的结果，但采用RestController请求方式一般会采用Restful风格的形式。
 *
 * Controller的作用：声明该类是Controller层的Bean，将该类声明进入Spring容器中进行管理
 *
 * ResponseBody的作用：表明该类的所有方法的返回值都直接进行提交而不经过视图解析器，且返回值的数据自动封装为json的数据格式
 *
 * RestController的作用：包含上面两个的作用，且支持Restful风格的数据提交方式
 *
 * Restful风格：
 *
 * get:获取数据时用的请求方式
 *
 * post：增加数据时的请求方式
 *
 * put：更新数据时的请求方式
 */
@RestController
@RequestMapping("login")
public class LoginController {
//    @Autowired
//    private SysUserService sysUserService

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginParam loginParam){
        //登录 验证用户  访问用户表
        return loginService.login(loginParam);
    }
}
