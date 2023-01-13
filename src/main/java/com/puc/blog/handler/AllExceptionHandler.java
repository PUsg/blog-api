package com.puc.blog.handler;


import com.puc.blog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


//对加了@ControllerAdvice 注解方法 进行拦截的处理    AOP
@ControllerAdvice
public class AllExceptionHandler {

    //进行异常处理 处理Exception.class的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody //返回json数据
    public Result doException(Exception ex){
        ex.printStackTrace();
        return Result.fail(-999,"系统异常");
    }

}
