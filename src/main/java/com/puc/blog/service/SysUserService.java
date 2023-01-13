package com.puc.blog.service;


import com.puc.blog.dao.pojo.SysUser;
import com.puc.blog.vo.Result;
import com.puc.blog.vo.UserVo;

public interface SysUserService {

    UserVo findUserVoById(Long id);

    SysUser findSysUserById(Long userId);

    SysUser findUser(String account, String password);

    /**
     * 根据token 查询用户信息
     * @param token
     * @return
     */
    Result findUserByToken(String token);

    /**
     * 根据账户查找用户
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);

    /**
     * 保存用户
     * @param sysUser
     */
    void save(SysUser sysUser);
}
