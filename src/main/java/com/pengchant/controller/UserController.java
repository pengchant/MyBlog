package com.pengchant.controller;

import com.pengchant.annotations.UserLoginToken;
import com.pengchant.model.BlogUser;
import com.pengchant.service.IUserService;
import com.pengchant.utils.JSONResult;
import com.pengchant.utils.MyToken;
import com.pengchant.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Api(value = "用户相关接口", tags = {"执行用户信息需求"})
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public RedisOperator REDIS;

    @Autowired
    public MyToken TOKEN;

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "查询所有用户信息", notes = "用户信息查询的接口,需要用户先登录")
    @PostMapping("/getAll")
    @UserLoginToken
    public JSONResult findAllUser(@RequestBody Map reqData) {
        String pageNum = (String) reqData.get("pageNum");
        String pageSize = (String) reqData.get("pageSize");
        int pNum = Integer.valueOf(pageNum);
        int pSize = Integer.valueOf(pageSize);
        logger.info("pageNO:" + pageNum + ", pageSize:" + pageSize);
        return JSONResult.ok(userService.findAllUser(pNum, pSize));
    }

    @ApiOperation(value = "登录", notes = "登录接口")
    @PostMapping("/login")
    public JSONResult login(@RequestBody @Valid BlogUser user) {
        try {
            BlogUser usr = userService.findUserById(user.getUserId());
            if (usr == null) { // 用户不存在
                return JSONResult.errorMsg("登录失败，用户不存在");
            } else {
                if (!(usr.getPwd().equals(user.getPwd()))) { // 密码错误
                    return JSONResult.errorMsg("登录失败，密码错误");
                } else {
                    // 1.如果登录成功,设置redis,0分钟
                    REDIS.set(REDIS.USER_REDIS_SESSION+":"+user.getUserId(),
                            UUID.randomUUID().toString(),
                            1000*30*60);
                    // 2.然后生成Token返回客户端
                    String token =TOKEN.createJWT("JWT"+user.getUserId(),
                            String.valueOf(user.getUserId()), user.getPwd());
                    // 清空密码
                    usr.setPwd("");
                    // 设置token
                    usr.setToken(token);
                    return JSONResult.ok(usr);
                }
            }
        } catch (Exception e) {
            logger.error("登录接口异常:" + e.getMessage());
        }
        return JSONResult.errorMsg("mblog:服务暂时不可用");
    }


    @ApiOperation(value = "用户注销",notes = "用户注销的接口")
    @ApiImplicitParam(name = "userId",value = "用户Id",required = true,
            dataType = "String",paramType = "query")
    @PostMapping("/logout")
    @UserLoginToken
    public JSONResult logOut(String userId) throws Exception{
        logger.info("====>delset:" + REDIS.USER_REDIS_SESSION+":"+userId);
        REDIS.del(REDIS.USER_REDIS_SESSION+":"+userId);
        return JSONResult.ok();
    }


}
