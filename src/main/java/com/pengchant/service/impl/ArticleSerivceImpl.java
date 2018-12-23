package com.pengchant.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pengchant.form.ArticleDetailForm;
import com.pengchant.form.CommentsForm;
import com.pengchant.form.UserArticlesForm;
import com.pengchant.mapper.ArticleMapper;
import com.pengchant.mapper.BlogUserMapper;
import com.pengchant.mapper.CommentMapper;
import com.pengchant.model.Article;
import com.pengchant.model.BlogUser;
import com.pengchant.model.Comment;
import com.pengchant.model.User;
import com.pengchant.service.IArticleService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service(value = "articleService")
public class ArticleSerivceImpl implements IArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleSerivceImpl.class);

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private BlogUserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;


    /**
     * 编写文章
     *
     * @param article
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean writeArticle(Article article) {
        // 插入数据
        int result = articleMapper.insertSelective(article);
        // 跟新用户的发文数
        boolean result2 = this.updateArticles(String.valueOf(article.getSuber()));
        return (result > 0) && result2 ? true : false;
    }

    /**
     * 更新文章数量
     *
     * @param myid
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateArticles(String myid) {
        boolean result = false;
        try {
            if (StringUtils.isNotBlank(myid)) {
                return (userMapper.updatemyArticles(myid) > 0);
            }
        } catch (NumberFormatException e) {
        }
        return result;
    }

    /**
     * 根据用户的id查询文章并且按照时间降序排列
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PageInfo<Article> queryMyArticles(String userid, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<Article> pagedArticle = new PageInfo<>(articleMapper.queryUserArticle(userid));
        return pagedArticle;
    }

    /**
     * 根据文章的id查询文章的详细信息
     *
     * @param articleId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ArticleDetailForm queryArticleForm(String articleId) {
        ArticleDetailForm articleDetailForm = null;
        try {
            // 1.查询文章实体
            Article myarticle = articleMapper.selectByPrimaryKey(Integer.valueOf(articleId));
            // 2.查询文章撰写者信息
            BlogUser writer = userMapper.selectByPrimaryKey(myarticle.getSuber());

            // 3.封装成article视图
            articleDetailForm = new ArticleDetailForm();
            articleDetailForm.setArcid(String.valueOf(myarticle.getArticleId()));
            articleDetailForm.setTitle(myarticle.getTitle());
            articleDetailForm.setSubtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                    format(myarticle.getSubtime()));
            articleDetailForm.setViewed(String.valueOf(myarticle.getViewed()));
            articleDetailForm.setContent(myarticle.getContent());
            articleDetailForm.setSuberid(String.valueOf(myarticle.getSuber()));
            articleDetailForm.setSubername(writer.getNickname());
            articleDetailForm.setDescrib(String.valueOf(myarticle.getDescribtion()));
            // 设置评论
            List<CommentsForm> commentsForms = queryAllComments(articleId);
            articleDetailForm.setArticleComments(commentsForms);
        } catch (NumberFormatException e) {
            logger.info("异常，" + e.getMessage());
        }
        return articleDetailForm;
    }

    /**
     * 添加评论
     *
     * @param comment
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean addComments(Comment comment) {
        int result = commentMapper.insertSelective(comment);
        return result > 0 ? true : false;
    }


    /**
     * 查询所有评论
     *
     * @param articleid
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CommentsForm> queryAllComments(String articleid) {
        List<CommentsForm> commentsForms = null;
        try {
            List<Map<String, Object>> comments = commentMapper.selectComments(articleid);
            if (comments != null && comments.size() > 0) {
                commentsForms = new ArrayList<>();
                // 遍历comments
                for (Map e : comments) {
                    CommentsForm commForm = new CommentsForm();
                    commForm.setCommid(String.valueOf(e.get("commentid")));
                    commForm.setArticleid(String.valueOf(e.get("articleid")));
                    commForm.setComments(String.valueOf(e.get("content")));
                    commForm.setSubtime(String.valueOf(e.get("subtime")));
                    commForm.setSuberid(String.valueOf(e.get("suberid")));
                    commForm.setSubername(String.valueOf(e.get("suber")));
                    commForm.setSuberimgurl(String.valueOf(e.get("headimgurl")));
                    // 添加到list中
                    commentsForms.add(commForm);
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return commentsForms;
    }

    /**
     * 查询所有文章列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PageInfo<UserArticlesForm> queryAllUserArticles(int pageNum, int pageSize) {
        // 分页查询
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<UserArticlesForm> pagedArticle = new PageInfo<>(articleMapper.queryAllArticle());
        return pagedArticle;
    }


    /**
     * 增加阅读数量
     *
     * @param articleId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean increaseViewed(int articleId) {
        boolean flag = false;
        Article article = articleMapper.selectByPrimaryKey(articleId);
        if (article != null) {
            article.setViewed(article.getViewed() + 1);
            articleMapper.updateByPrimaryKeySelective(article);
            flag = true;
        }
        return flag;
    }

    /**
     * 删除文章
     *
     * @param articleId
     * @param suberid
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean deleteArticle(int articleId, int suberid) {
        int flag = 0;
        Article article = articleMapper.selectByPrimaryKey(articleId);
        if (article != null && article.getSuber() == suberid) {
            flag = articleMapper.deleteByPrimaryKey(articleId);
            // 更新文章的数量
            if(flag>0){
                return this.updateArticles(String.valueOf(suberid));
            }
        }
        return false;
    }
}
