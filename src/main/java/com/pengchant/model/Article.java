package com.pengchant.model;

import java.util.Date;

public class Article {
    private Integer articleId;

    private String title;

    private String describtion;

    private Date subtime;

    private Integer viewed;

    private Integer shared;

    private Integer greated;

    private Byte sts;

    private Integer suber;

    private String b1;

    private String b2;

    private String b3;

    private String content;

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDescribtion() {
        return describtion;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion == null ? null : describtion.trim();
    }

    public Date getSubtime() {
        return subtime;
    }

    public void setSubtime(Date subtime) {
        this.subtime = subtime;
    }

    public Integer getViewed() {
        return viewed;
    }

    public void setViewed(Integer viewed) {
        this.viewed = viewed;
    }

    public Integer getShared() {
        return shared;
    }

    public void setShared(Integer shared) {
        this.shared = shared;
    }

    public Integer getGreated() {
        return greated;
    }

    public void setGreated(Integer greated) {
        this.greated = greated;
    }

    public Byte getSts() {
        return sts;
    }

    public void setSts(Byte sts) {
        this.sts = sts;
    }

    public Integer getSuber() {
        return suber;
    }

    public void setSuber(Integer suber) {
        this.suber = suber;
    }

    public String getB1() {
        return b1;
    }

    public void setB1(String b1) {
        this.b1 = b1 == null ? null : b1.trim();
    }

    public String getB2() {
        return b2;
    }

    public void setB2(String b2) {
        this.b2 = b2 == null ? null : b2.trim();
    }

    public String getB3() {
        return b3;
    }

    public void setB3(String b3) {
        this.b3 = b3 == null ? null : b3.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}