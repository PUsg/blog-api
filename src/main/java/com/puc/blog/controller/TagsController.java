package com.puc.blog.controller;


import com.puc.blog.service.TagsService;
import com.puc.blog.vo.Result;
import com.puc.blog.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // RestController 代表返回的是一个json数据
@RequestMapping("tags") //路径映射
public class TagsController {

    @Autowired
    private TagsService tagsService;

    // /tags/hot
    @GetMapping("hot")
    public Result hot(){
        int limit = 6;
        return tagsService.hot(limit);
    }
    @GetMapping
    public Result findAll(){
        return tagsService.findAll();
    }

    @GetMapping("detail")
    public Result findAllDetail(){
        return tagsService.findAllDetail();
    }
    @GetMapping("detail/{id}")
    public Result findDetailById(@PathVariable("id") Long id){
        return tagsService.findDetailById(id);
    }
}
