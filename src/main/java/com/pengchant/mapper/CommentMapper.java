package com.pengchant.mapper;

import com.pengchant.model.Comment;

import java.util.List;
import java.util.Map;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer commentId);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer commentId);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    /**
     * 查询某文章的所有评论
     * @param articleid
     * @return
     */
    List<Comment> selectAllArtiComm(String articleid);

    /**
     * 查询文章的评论
     * @param articleid
     * @return
     */
    List<Map<String, Object>> selectComments (String articleid);

}