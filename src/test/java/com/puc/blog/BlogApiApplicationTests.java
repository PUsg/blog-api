package com.puc.blog;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.puc.blog.dao.mapper.ArticleMapper;
import com.puc.blog.dao.pojo.Article;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class BlogApiApplicationTests {

    @Resource
    ArticleMapper articleMapper;
    @Test
    void contextLoads() {
        List<Article> list = articleMapper.selectList(new QueryWrapper<>());
        System.out.println(list);
    }

}
