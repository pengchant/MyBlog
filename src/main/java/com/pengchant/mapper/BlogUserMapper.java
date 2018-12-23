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
     * 根据用户nickname/email查询用户的信息
     * @param searchStr
     * @return
     */
    long searchByNickEm(String searchStr);

    /**
     * 查询用户登录实体
     * @param accstr
     * @return
     */
    List<BlogUser> queryLogin(String accstr);

    /**
     * 更新文章数量
     * @param userid
     * @return
     */
    long updateArticles(String userid);

    /**
     * 更新我的文章的数量
     * @param userid
     * @return
     */
    long updatemyArticles(String userid);

    /**
     * 更新人脉
     * @param userid
     * @return
     */
    long updateRenMai(String userid);


}