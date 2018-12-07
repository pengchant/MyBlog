package com.pengchant.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel(value = "用户对象", description = "用户实体")
public class BlogUser {

    @ApiModelProperty(hidden = true)
    private Integer userId;

    private String nickname;

    @NotNull(message = "用户的密码不能为空")
    private String pwd;

    private String phonenum;

    private Byte pChked;

    private String email;

    private Byte eCheked;

    private String qq;

    private String weixinid;

    private String weiboid;

    private Byte sts;

    private String headimgurl;

    private Integer friends;

    private Integer articles;

    @JsonIgnore
    private String b1;

    @JsonIgnore
    private String b2;

    @JsonIgnore
    private String b3;


    @ApiModelProperty(hidden = true)
    private String token;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum == null ? null : phonenum.trim();
    }

    public Byte getpChked() {
        return pChked;
    }

    public void setpChked(Byte pChked) {
        this.pChked = pChked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Byte geteCheked() {
        return eCheked;
    }

    public void seteCheked(Byte eCheked) {
        this.eCheked = eCheked;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public String getWeixinid() {
        return weixinid;
    }

    public void setWeixinid(String weixinid) {
        this.weixinid = weixinid == null ? null : weixinid.trim();
    }

    public String getWeiboid() {
        return weiboid;
    }

    public void setWeiboid(String weiboid) {
        this.weiboid = weiboid == null ? null : weiboid.trim();
    }

    public Byte getSts() {
        return sts;
    }

    public void setSts(Byte sts) {
        this.sts = sts;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl == null ? null : headimgurl.trim();
    }

    public Integer getFriends() {
        return friends;
    }

    public void setFriends(Integer friends) {
        this.friends = friends;
    }

    public Integer getArticles() {
        return articles;
    }

    public void setArticles(Integer articles) {
        this.articles = articles;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}