package com.puc.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // 自动生成所有的构造方法
public class Result {

    private boolean success;

    private int code;

    private String msg;

    private Object data;

    public static Result success(Object data){
        return new Result(true,200,"success", data);
    }

    public static Result fail(int code, String msg){
        return new Result(false,code,msg, null);
    }


}
