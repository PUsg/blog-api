package com.puc.blog.service;

import com.puc.blog.vo.ArticleVo;
import com.puc.blog.vo.Result;
import com.puc.blog.vo.params.ArticleParam;
import com.puc.blog.vo.params.PageParams;

import java.util.List;

public interface ArticleService {


//    /**
//     * 分页查询 文章列表
//     *
//     * @param pageParams
//     * @return
//     */
//    List<ArticleVo> listArticlesPage(PageParams pageParams);

    /**
     * 分页查询 文章列表
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);

    /**
     * 最热文章
     * @param limit
     * @return
     */
    Result hotArticle(int limit);

    /**
     * 最新文章
     * @param limit
     * @return
     */
    Result newArticles(int limit);

    /**
     * 文章归档
     * @return
     */
    Result listArchives();

    /**
     * 查看文章详情
     * @param articleId
     * @return
     */
    Result findArticleById(Long articleId);

    /**
     * 文章发布服务
     * @param articleParam
     * @return
     */
    Result publish(ArticleParam articleParam);


}
