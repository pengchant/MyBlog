package com.pengchant.form;

import java.util.List;

/**
 * 文章具体内容表单
 */
public class ArticleDetailForm {

    /**
     * 文章id
     */
    private String arcid;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章简述
     */
    private String describ;

    /**
     * 文章提交者id
     */
    private String suberid;

    /**
     * 文章提交者昵称
     */
    private String subername;

    /**
     * 文章提交时间
     */
    private String subtime;

    /**
     * 文章查看次数
     */
    private String viewed;

    /**
     * 文章主题内容
     */
    private String content;

    /**
     * 文章评论
     */
    private List<CommentsForm> articleComments;

    public String getArcid() {
        return arcid;
    }

    public void setArcid(String arcid) {
        this.arcid = arcid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ;
    }

    public String getSuberid() {
        return suberid;
    }

    public void setSuberid(String suberid) {
        this.suberid = suberid;
    }

    public String getSubername() {
        return subername;
    }

    public void setSubername(String subername) {
        this.subername = subername;
    }

    public String getSubtime() {
        return subtime;
    }

    public void setSubtime(String subtime) {
        this.subtime = subtime;
    }

    public String getViewed() {
        return viewed;
    }

    public void setViewed(String viewed) {
        this.viewed = viewed;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<CommentsForm> getArticleComments() {
        return articleComments;
    }

    public void setArticleComments(List<CommentsForm> articleComments) {
        this.articleComments = articleComments;
    }
}
