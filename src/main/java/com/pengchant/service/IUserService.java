package com.pengchant.service;

import com.github.pagehelper.PageInfo;
import com.pengchant.form.LoginForm;
import com.pengchant.form.UserRegistForm;
import com.pengchant.model.BlogUser;
import com.pengchant.model.User;

/**
 * 用户的服务类
 */
public interface IUserService {

    PageInfo<User> findAllUser(int pageNum, int pageSize);


    BlogUser findUserById(int userId);

    /**
     * 根据nickname查询是否存在此人
     * @param nickname
     * @return
     */
    boolean findExistsUser(String nickname);

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
}
