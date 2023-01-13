package com.puc.blog.service;

import com.puc.blog.dao.pojo.SysUser;
import com.puc.blog.vo.Result;
import com.puc.blog.vo.params.LoginParam;


public interface LoginService {
    /**
     * 登录功能
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    SysUser checkToken(String token);

    /**
     * 退出登录
     * @param token
     * @return
     */
    Result logout(String token);

    /**
     * 注册
     * @param loginParam
     * @return
     */
    Result register(LoginParam loginParam);
}
