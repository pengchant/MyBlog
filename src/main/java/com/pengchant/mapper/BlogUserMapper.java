package com.pengchant.mapper;

import com.pengchant.model.BlogUser;

import java.util.List;

public interface BlogUserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(BlogUser record);

    int insertSelective(BlogUser record);

    BlogUser selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(BlogUser record);

    int updateByPrimaryKey(BlogUser record);

    /**
     * 根据用户nickname查询用户的信息
     * @param nickname
     * @return
     */
    long searchByNick(String nickname);

    /**
     * 查询用户登录实体
     * @param accstr
     * @return
     */
    List<BlogUser> queryLogin(String accstr);


}