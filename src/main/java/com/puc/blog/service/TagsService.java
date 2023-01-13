package com.puc.blog.service;

import com.puc.blog.vo.Result;
import com.puc.blog.vo.TagVo;

import java.util.List;

public interface TagsService {
    /**
     * 根据文章id查询标签列表
     * @param id
     * @return
     */
    List<TagVo> findTagsByArticleId(Long id);

    Result hot(int limit);

    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);
}
