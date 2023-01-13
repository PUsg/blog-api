package com.puc.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.puc.blog.dao.pojo.SysUser;
import com.puc.blog.service.LoginService;
import com.puc.blog.service.SysUserService;
import com.puc.blog.util.JWTUtils;
import com.puc.blog.vo.ErrorCode;
import com.puc.blog.vo.Result;
import com.puc.blog.vo.params.LoginParam;
import io.netty.util.internal.StringUtil;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    @Autowired
    @Lazy
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String salt = "puc!@###";

//    public static void main(String[] args) {
//        LoginParam loginParam = new LoginParam();
//        loginParam.setAccount("admin");
//        loginParam.setPassword("admin");
//        LoginServiceImpl loginService = new LoginServiceImpl();
//        System.out.println(loginService.login(loginParam));
//    }


    @Override
    public Result login(LoginParam loginParam) {
        /**
         *  1.检查参数是否合法
         *  2.根据用户名和密码去user表中查询
         *  3.不存在 登陆失败
         *  4.存在 使用jwt生成token 返回前端
         *  5.token 放入 redis 当中 token： user信息 设置过期时间
         *  （登录认证的时候 先认证token 字符串是否合法 去 redis 认证是否存在）
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        password = DigestUtils.md5Hex(password + salt);
        SysUser sysUser = sysUserService.findUser(account,password);
        if(sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        String token = JWTUtils.createToken(sysUser.getId());

        redisTemplate.opsForValue().set("TOKEN_" + token , JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if(stringObjectMap == null){
            return null;
        }

        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if(StringUtils.isEmpty(userJson)){
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_ " + token);
        return Result.success(null);
    }

    @Override
    public Result register(LoginParam loginParam) {
        /**
         * 1.判断参数是否合法
         * 2.判断账户是否存在，存在返回账户已经被注册
         * 3.不存在 注册账户
         * 4.生成token
         * 5.存入redis
         * 6.注意 加上事务  一旦中间的任何过程出现问题 注册的用户 需要回滚  使用 @Transactional
         */
        //1.判断参数是否合法
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        if(StringUtils.isEmpty(account)
            || StringUtils.isEmpty(password)
            || StringUtils.isEmpty(nickname)
        ){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        //         * 2.判断账户是否存在，存在返回账户已经被注册
        SysUser sysUser = sysUserService.findUserByAccount(account);
        if(sysUser != null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        //         * 3.不存在 注册账户
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password + salt));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1);
        sysUser.setDeleted(0);
        sysUser.setStatus("");
        sysUser.setSalt("");
        this.sysUserService.save(sysUser);
        String token = JWTUtils.createToken(sysUser.getId());
//        redisTemplate.opsForValue();//操作字符串
//        redisTemplate.opsForHash();//操作hash
//        redisTemplate.opsForList();//操作list
//        redisTemplate.opsForSet();//操作set
//        redisTemplate.opsForZSet();//操作有序set
        //JSON.toJSONString(sysUser) 是将 sysUser 这个HashMap类型的数据返回成json字符串
        //目前为止  只把登录过程中的 token 存到 redis 中
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser),1,TimeUnit.DAYS);
        return Result.success(token);
    }
}
