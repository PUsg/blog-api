package com.puc.blog.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.puc.blog.dao.dos.Archives;
import com.puc.blog.dao.mapper.ArticleBodyMapper;
import com.puc.blog.dao.mapper.ArticleMapper;
import com.puc.blog.dao.mapper.ArticleTagMapper;
import com.puc.blog.dao.pojo.Article;
import com.puc.blog.dao.pojo.ArticleBody;
import com.puc.blog.dao.pojo.ArticleTag;
import com.puc.blog.dao.pojo.SysUser;
import com.puc.blog.service.*;
import com.puc.blog.util.UserThreadLocal;
import com.puc.blog.vo.ArticleBodyVo;
import com.puc.blog.vo.ArticleVo;
import com.puc.blog.vo.Result;
import com.puc.blog.vo.TagVo;
import com.puc.blog.vo.params.ArticleParam;
import com.puc.blog.vo.params.PageParams;

import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private TagsService tagsService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    public ArticleVo copy(Article article,boolean isAuthor,boolean isBody,boolean isTags,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article, articleVo);
//        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));  //把数据库中的create_date 从Long 换成Date 不需要这行代码

        //并不是所有接口 都需要标签，作者信息
        if (isAuthor) {
            SysUser sysUser = sysUserService.findSysUserById(article.getAuthorId());
            articleVo.setAuthor(sysUser.getNickname());
        }
        if (isTags){
            List<TagVo> tags = tagsService.findTagsByArticleId(article.getId());
            articleVo.setTags(tags);
        }
        if(isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if(isCategory){
            Long categoryId =article.getCategoryId();
            articleVo.setCategory(categoryService.finCategoryById(categoryId));
        }
        return articleVo;
    }

    @Autowired
    ArticleBodyMapper articleBodyMapper;

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    private List<ArticleVo> copyList(List<Article> records,boolean isAuthor,boolean isBody,boolean isTags) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article article : records) {
            ArticleVo articleVo = copy(article,isAuthor,isBody,isTags,false);
            articleVoList.add(articleVo);
        }
        return articleVoList;
    }
    private List<ArticleVo> copyList(List<Article> records,boolean isAuthor,boolean isBody,boolean isTags,boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article article : records) {
            ArticleVo articleVo = copy(article,isAuthor,isBody,isTags,isCategory);
            articleVoList.add(articleVo);
        }
        return articleVoList;
    }

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = this.articleMapper.listArticle(page,pageParams.getCategoryId(),pageParams.getTagId(),pageParams.getYear(),pageParams.getMonth());
        return Result.success(copyList(articleIPage.getRecords(),true,false,true));
    }
//    @Override
//    public Result listArticle(PageParams pageParams) {
//        // 做首页的文章展示  需要一个参数 来 确定每页有多少条 一共有多少页
//
//        /**
//         * 1.分页查询 article 数据表
//         */
//        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        if (pageParams.getCategoryId() != null) {
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        List<Long> articleIdList = new ArrayList<>();
//        if(pageParams.getTagId() != null){
//            //加入标签 条件查询
//            //article表中 并没有tag字段 一篇文章 有多个标签
//            //article_tag article_id 1 : n tag_id
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for(ArticleTag articleTag : articleTags){
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if(articleIdList.size() > 0){
//                //and id in(1,2,3)
//                queryWrapper.in(Article::getId,articleIdList);
//            }
//        }
////        if(pageParams.getYear().length() > 0 && pageParams.getMonth().length() > 0){
////
////            queryWrapper.eq(Article::getCreateDate,pageParams.getYear());
////            queryWrapper.eq(Article::getCreateDate,pageParams.getMonth());
////        }
//        //是否置顶排序
//        // 文章根据创建时间进行倒叙排列
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);  // queryWrapper 来确定查询的规则
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);  //返回的是MybatisPlus自带的Page类
//        List<Article> records = articlePage.getRecords(); // 用这个类中的getRecords方法 返回一个List集合
//
//        //不能直接返回 要将数据 转换成 json格式
//        //ArticleVo 就是 封装类
//        List<ArticleVo> articleVoList = copyList(records,true,false,true);
//        return Result.success(articleVoList);
//    }



    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts); // 对浏览量进行倒叙排列
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit " + limit); // "limit " + limit 要记得加空格
        // select id, title from article order by view_counts desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles, false,false,false));
    }

    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate); // 对创建时间进行倒叙排列
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit " + limit); // "limit " + limit 要记得加空格
        // select id, title from article order by creat_date desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles, false,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Autowired
    private ThreadService threadService;

    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1.根据Id 查询文章信息
         * 2.根据Body Id 和 categoryId 去做关联查询
         */

        Article article = articleMapper.selectById(articleId);
        ArticleVo articleVo= copy(article,true,true,true,true);

        //查看完文章之后 本应该直接返回数据了， 这时候做了一个更新操作，更新时加写锁，阻塞其他的读操作，性能就会比较低 没办法优化
        //更新 增加了 此次接口的耗时 一旦更新出问题不能影响查看文章的操作
        //线程池 可以把更新操作 扔到线程池中去执行  和主线程就不相关了
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }

    @Override
    @Transactional
    public Result publish(ArticleParam articleParam) {
        /**
         * 1.发布文章 目的是构建article对象
         * 2.作者id 当前的登录用户
         * 3.标签 要将标签加入到关联列表当中
         * 4.body 内容存储
         */
        SysUser sysUser = UserThreadLocal.get();
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(new Date());
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        //插入之后会生成一个文章id
        this.articleMapper.insert(article);
        //tag
        List<TagVo> tags = articleParam.getTags();
        if(tags != null){
            for(TagVo tag : tags){
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        //body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        Map<String,String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return Result.success(map);
    }

}