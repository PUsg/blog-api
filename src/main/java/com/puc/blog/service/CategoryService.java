package com.puc.blog.service;

import com.puc.blog.vo.CategoryVo;
import com.puc.blog.vo.Result;

import java.util.List;

public interface CategoryService {
    CategoryVo finCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();


    Result categoriesDetailById(Long id);
}
