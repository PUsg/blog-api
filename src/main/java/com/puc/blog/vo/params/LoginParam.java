package com.puc.blog.vo.params;


import com.puc.blog.vo.Result;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;

@Data
public class LoginParam {

    private String account;

    private String password;

    private String nickname;

}
