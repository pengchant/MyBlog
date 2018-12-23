package com.pengchant.model;

import java.util.Date;

public class Comment {
    private Integer commentId;

    private String content;

    private Date subtime;

    private Byte sts;

    private Integer lev;

    private String suber;

    private Integer suberid;

    private Integer articleid;

    private String b1;

    private String b2;

    private String b3;

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getSubtime() {
        return subtime;
    }

    public void setSubtime(Date subtime) {
        this.subtime = subtime;
    }

    public Byte getSts() {
        return sts;
    }

    public void setSts(Byte sts) {
        this.sts = sts;
    }

    public Integer getLev() {
        return lev;
    }

    public void setLev(Integer lev) {
        this.lev = lev;
    }

    public String getSuber() {
        return suber;
    }

    public void setSuber(String suber) {
        this.suber = suber == null ? null : suber.trim();
    }

    public Integer getSuberid() {
        return suberid;
    }

    public void setSuberid(Integer suberid) {
        this.suberid = suberid;
    }

    public Integer getArticleid() {
        return articleid;
    }

    public void setArticleid(Integer articleid) {
        this.articleid = articleid;
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
}