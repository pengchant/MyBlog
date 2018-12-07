package com.pengchant.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pengchant.form.LoginForm;
import com.pengchant.form.UserRegistForm;
import com.pengchant.mapper.BlogUserMapper;
import com.pengchant.mapper.UserMapper;
import com.pengchant.model.BlogUser;
import com.pengchant.model.User;
import com.pengchant.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务的功能实现类
 */
@Service(value = "userService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BlogUserMapper blogUserMapper;


    /**
     * 查询所有的用户
     *
     * @param pageNum  页号
     * @param pageSize 页面数据大小
     * @return 返回用户的列表信息
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PageInfo<User> findAllUser(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<User> pagedUser = new PageInfo<>(userMapper.selectAllUser());
        return pagedUser;
    }

    @Override
    public BlogUser findUserById(int userId) {
        return blogUserMapper.selectByPrimaryKey(userId);
    }


    /**
     * 根据用户nickname查询用户信息
     *
     * @param nickname
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean findExistsUser(String nickname) {
        long result = blogUserMapper.searchByNick(nickname);
        return result > 0 ? true : false;
    }

    /**
     * 添加用户信息
     *
     * @param blogUser
     * @return
     */
    @Override
    public boolean addBlogUser(BlogUser blogUser) {
        long result = blogUserMapper.insertSelective(blogUser);
        return result > 0 ? true : false;
    }


    /**
     * 查询用户登录
     *
     * @param loginForm
     * @return
     */
    @Override
    public BlogUser userLogin(LoginForm loginForm) {
        BlogUser user = null;
        List<BlogUser> results = blogUserMapper.queryLogin(loginForm.getAccount());
        if (results != null && results.size() > 0) {
            user = results.get(0);
        }
        return user;
    }
}
