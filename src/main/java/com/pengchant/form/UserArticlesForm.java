package com.pengchant.form;

/**
 * 用户首页文章列表
 */
public class UserArticlesForm {

    /**
     * 文章编号
     */
    private String articleid;

    /**
     * 提交者编号
     */
    private String suberid;

    /**
     * 用户头像url
     */
    private String imgurl;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章描述
     */
    private String describtion;

    /**
     * 提交时间
     */
    private String subtime;


    /**
     * 阅读量
     */
    private String viewed;

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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribtion() {
        return describtion;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion;
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
}
