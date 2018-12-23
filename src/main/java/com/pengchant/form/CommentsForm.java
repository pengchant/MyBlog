package com.pengchant.form;

/**
 * 评论表单
 */
public class CommentsForm {

    /**
     * 评论编号
     */
    private String commid;

    /**
     * 文章编号
     */
    private String articleid;

    /**
     * 评论编号
     */
    private String suberid;

    /**
     * 评论人
     */
    private String subername;

    /**
     * 评论人头像url
     */
    private String suberimgurl;

    /**
     * 评论内容
     */
    private String comments;

    /**
     * 评论提交时间
     */
    private String subtime;


    public String getArticleid() {
        return articleid;
    }

    public void setArticleid(String articleid) {
        this.articleid = articleid;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSubtime() {
        return subtime;
    }

    public void setSubtime(String subtime) {
        this.subtime = subtime;
    }

    public String getCommid() {
        return commid;
    }

    public void setCommid(String commid) {
        this.commid = commid;
    }

    public String getSuberimgurl() {
        return suberimgurl;
    }

    public void setSuberimgurl(String suberimgurl) {
        this.suberimgurl = suberimgurl;
    }
}
