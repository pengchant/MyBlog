package com.pengchant.controller;

import com.pengchant.service.IUserService;
import com.pengchant.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Api(value = "用户相关接口", tags = {"执行用户信息需求"})
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "查询所有用户信息",notes = "用户信息查询的接口")
    @PostMapping("/getAll")
    public JSONResult findAllUser(@RequestBody Map reqData) {
        String pageNum = (String) reqData.get("pageNum");
        String pageSize = (String) reqData.get("pageSize");
        int pNum = Integer.valueOf(pageNum);
        int pSize = Integer.valueOf(pageSize);
        logger.info("pageNO:" + pageNum + ", pageSize:" + pageSize);
        return JSONResult.ok(userService.findAllUser(pNum, pSize));
    }

}
