package com.pengchant.form;

/**
 * 我的粉丝
 */
public class MyFansUser {

    /**
     * 用户的编号
     */
    private String userid;

    /**
     * 头像的url
     */
    private String headimgurl;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户的标（是否已经关注）
     */
    private String flag;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
