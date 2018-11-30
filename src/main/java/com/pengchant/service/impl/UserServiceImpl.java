package com.pengchant.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pengchant.mapper.UserMapper;
import com.pengchant.model.User;
import com.pengchant.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务的功能实现类
 */
@Service(value = "userService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;


    /**
     * 查询所有的用户
     * @param pageNum   页号
     * @param pageSize  页面数据大小
     * @return          返回用户的列表信息
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PageInfo<User> findAllUser(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<User> pagedUser = new PageInfo<>(userMapper.selectAllUser());
        return pagedUser;
    }
}
