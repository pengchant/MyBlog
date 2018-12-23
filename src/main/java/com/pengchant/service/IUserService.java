package com.pengchant.service;

import com.github.pagehelper.PageInfo;
import com.pengchant.form.LoginForm;
import com.pengchant.form.MyCaredUser;
import com.pengchant.form.MyFansUser;
import com.pengchant.form.UserRegistForm;
import com.pengchant.model.BlogUser;
import com.pengchant.model.User;

import java.util.List;

/**
 * 用户的服务类
 */
public interface IUserService {

    PageInfo<User> findAllUser(int pageNum, int pageSize);


    BlogUser findUserById(int userId);

    /**
     * 根据nickname/email查询是否存在此人
     * @param searchStr
     * @return
     */
    boolean findExistsUser(String searchStr);

    /**
     * 添加bloguser
     * @param blogUser
     * @return
     */
    boolean addBlogUser(BlogUser blogUser);

    /**
     * 查询用户登录实体
     * @param loginForm
     * @return
     */
    BlogUser userLogin(LoginForm loginForm);

    /**
     * 根据id获取用户信息
     * @param userId
     * @return
     */
    BlogUser getUserInfo(String userId);

    /**
     * 保存用户的头像路径
     * @param usrid
     * @param imgurl
     * @return
     */
    BlogUser saveUserImgUrl(int usrid, String imgurl);


    /**
     * 查询我所有的关注
     * @param myid
     * @return
     */
    List<MyCaredUser> allMyCared(String myid);

    /**
     * 取消关注blgid
     * @param myid
     * @param blgid
     * @return
     */
    boolean cancelCared(String myid, String blgid);

    /**
     * 查询我的粉丝
     * @param myid
     * @return
     */
    List<MyFansUser> allMyFans(String myid);

    /**
     * 追加关注
     * @param caredid
     * @param myid
     * @return
     */
    boolean addCared(String caredid, String myid);

    /**
     * 查询是否已经关注过某人
     * @param myid
     * @param blgid
     * @return
     */
    boolean hasCared(String myid, String blgid);



    /**
     * 更新我的朋友数量
     * @param myid
     * @return
     */
    boolean updateFriends(String myid);
}
