package com.pengchant.service;

import com.github.pagehelper.PageInfo;
import com.pengchant.model.BlogUser;
import com.pengchant.model.User;

/**
 * 用户的服务类
 */
public interface IUserService {

    PageInfo<User> findAllUser(int pageNum, int pageSize);


    BlogUser findUserById(int userId);
}
