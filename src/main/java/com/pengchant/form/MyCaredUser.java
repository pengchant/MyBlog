package com.pengchant.form;

/**
 * 我的关注
 */
public class MyCaredUser {

    /**
     * 用户编号
     */
    private String userid;

    /**
     * 用户头像
     */
    private String headimgurl;

    /**
     * 昵称
     */
    private String nickname;

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
}
