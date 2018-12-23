package com.pengchant.service.impl;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pengchant.form.LoginForm;
import com.pengchant.form.MyCaredUser;
import com.pengchant.form.MyFansUser;
import com.pengchant.form.UserRegistForm;
import com.pengchant.mapper.BlogUserMapper;
import com.pengchant.mapper.FriendShipMapper;
import com.pengchant.mapper.UserMapper;
import com.pengchant.model.BlogUser;
import com.pengchant.model.User;
import com.pengchant.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BlogUserMapper blogUserMapper;

    @Autowired
    private FriendShipMapper friendShipMapper;

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
     * 根据用户searchStr查询用户信息
     *
     * @param searchStr
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean findExistsUser(String searchStr) {
        long result = blogUserMapper.searchByNickEm(searchStr);
        logger.info("====>result:" + result);
        return result > 0 ? true : false;
    }

    /**
     * 添加用户信息
     *
     * @param blogUser
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
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
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public BlogUser userLogin(LoginForm loginForm) {
        BlogUser user = null;
        List<BlogUser> results = blogUserMapper.queryLogin(loginForm.getAccount());
        if (results != null && results.size() > 0) {
            user = results.get(0);
        }
        return user;
    }

    /**
     * 根据id获取用户信息
     *
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public BlogUser getUserInfo(String userId) {
        return blogUserMapper.selectByPrimaryKey(Integer.valueOf(userId));
    }


    /**
     * 保存用户图片的路径
     *
     * @param usrid
     * @param imgurl
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BlogUser saveUserImgUrl(int usrid, String imgurl) {
        BlogUser usr = blogUserMapper.selectByPrimaryKey(usrid);
        if (usr != null) {
            usr.setHeadimgurl(imgurl);
            blogUserMapper.updateByPrimaryKeySelective(usr);
        }
        return usr;
    }

    /**
     * 查询我的关注
     *
     * @param myid
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<MyCaredUser> allMyCared(String myid) {
        return friendShipMapper.querymycared(myid);
    }

    /**
     * 取消关注
     *
     * @param myid
     * @param blgid
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean cancelCared(String myid, String blgid) {
        int result = friendShipMapper.cancelcared(myid, blgid);
        if(result>0){
            // 更新人脉
            return updateFriends(myid);
        }
        return false;
    }

    /**
     * 查询我的所有粉丝
     *
     * @param myid
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<MyFansUser> allMyFans(String myid) {
        return friendShipMapper.selectmyfans(myid);
    }

    /**
     * 添加关注
     *
     * @param caredid
     * @param myid
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean addCared(String caredid, String myid) {
        int result = friendShipMapper.addcared(caredid, myid);
        if(result>0){
            // 更新朋友数量
            return this.updateFriends(myid);
        }
        return false;
    }

    /**
     * 是否已经关注某人
     *
     * @param myid
     * @param blgid
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean hasCared(String myid, String blgid) {
        return friendShipMapper.hascared(myid, blgid) > 0;
    }



    /**
     * 更新朋友数量
     *
     * @param myid
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateFriends(String myid) {
        boolean result = false;
        try {
            if (StringUtils.isNotBlank(myid)) {
                return (blogUserMapper.updateRenMai(myid) > 0);
            }
        } catch (NumberFormatException e) {
        }
        return result;
    }
}
