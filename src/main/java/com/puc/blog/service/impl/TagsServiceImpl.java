package com.puc.blog.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.puc.blog.dao.mapper.TagMapper;
import com.puc.blog.dao.pojo.Tag;
import com.puc.blog.service.TagsService;
import com.puc.blog.vo.Result;
import com.puc.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagsServiceImpl implements TagsService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagVo> findTagsByArticleId(Long id) {
        //mybatis plus 无法进行多表查询
        List<Tag> tags = tagMapper.findTagsByArticleId(id);
        //要返回vo类型 所以要用copyList
        return copyList(tags);
    }

    @Override
    public Result hot(int limit) {

        /**
         * 1.标签 所拥有的文章数量最多
         * 2.查询
         */
        List<Long> hotsTagIds = tagMapper.findHotsTagIds(limit);

        //需求的是tagId 和 tag Name
        //这里查询tagName select * from tag where id in(1,2,3)
        if (CollectionUtils.isEmpty(hotsTagIds)) {
            return Result.success(Collections.emptyList());
        }
        List<Tag> tagList = tagMapper.findTagsByTagIds(hotsTagIds);
        return Result.success(tagList);
    }

    @Override
    public Result findAll() {
        List<Tag> tags = this.tagMapper.selectList(new LambdaQueryWrapper<>());
        return Result.success(copyList(tags));
    }

    @Override
    public Result findAllDetail() {
        List<Tag> tags = this.tagMapper.selectList(new LambdaQueryWrapper<Tag>());
        return Result.success(copyList(tags));
    }

    @Override
    public Result findDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        return Result.success(copy(tag));
    }

    public TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        tagVo.setId(String.valueOf(tag.getId()));
        return tagVo;
    }
    public List<TagVo> copyList(List<Tag> tagList){
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }


}