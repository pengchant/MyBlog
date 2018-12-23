package com.pengchant.mapper;

import com.pengchant.form.UserArticlesForm;
import com.pengchant.model.Article;

import java.util.List;
import java.util.Map;

public interface ArticleMapper {
    int deleteByPrimaryKey(Integer articleId);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Integer articleId);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKeyWithBLOBs(Article record);

    int updateByPrimaryKey(Article record);

    /**
     * 根据用户的id查询所有已经发布的文章按照时间降序排列
     * @param userid
     * @return
     */
    List<Article> queryUserArticle(String userid);

    /**
     * 查询首页列表
     * @return
     */
    List<UserArticlesForm> queryAllArticle();

}