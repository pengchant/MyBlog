package com.pengchant.service;


import com.github.pagehelper.PageInfo;
import com.pengchant.form.ArticleDetailForm;
import com.pengchant.form.CommentsForm;
import com.pengchant.form.UserArticlesForm;
import com.pengchant.model.Article;
import com.pengchant.model.Comment;

import java.util.List;

/**
 * 文章服务
 */
public interface IArticleService {

    /**
     * 编写文章服务
     * @param article
     * @return
     */
    boolean writeArticle(Article article);

    /**
     * 查询用户的文章按照发布的时间降序排，并且返回
     * @param userid
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Article> queryMyArticles(String userid, int pageNum, int pageSize);


    /**
     * 根据articleid查询文章的详细信息
     * @param articleId
     * @return
     */
    ArticleDetailForm queryArticleForm(String articleId);

    /**
     * 添加评论
     * @param comment
     * @return
     */
    boolean addComments(Comment comment);

    /**
     * 查询所有的评论
     * @param articleid
     * @return
     */
    List<CommentsForm> queryAllComments(String articleid);

    /**
     * 分页查询首页的文章列表
     * @return
     */
    PageInfo<UserArticlesForm> queryAllUserArticles(int pageNum, int pageSize);

    /**
     * 增加阅读数量
     * @param articleId
     * @return
     */
    boolean increaseViewed(int articleId);

    /**
     * 删除文章
     * @param articleId
     * @param suberid
     * @return
     */
    boolean deleteArticle(int articleId, int suberid);

    /**
     * 更新我的文章数量
     * @param myid
     * @return
     */
    boolean updateArticles(String myid);
}
