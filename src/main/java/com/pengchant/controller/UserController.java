package com.pengchant.controller;

import com.pengchant.annotations.UserLoginToken;
import com.pengchant.configure.MyEmailMsgConfig;
import com.pengchant.form.LoginForm;
import com.pengchant.form.UserRegistForm;
import com.pengchant.model.BlogUser;
import com.pengchant.service.IUserService;
import com.pengchant.utils.JSONResult;
import com.pengchant.utils.MyToken;
import com.pengchant.utils.RedisOperator;
import com.pengchant.utils.SecurityCode;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Api(value = "用户相关接口", tags = {"执行用户信息需求"})
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * reids 操作
     */
    @Autowired
    public RedisOperator REDIS;

    /**
     * token操作
     */
    @Autowired
    public MyToken TOKEN;

    /**
     * 用户服务
     */
    @Autowired
    private IUserService userService;

    /**
     * 发送邮件的javamailsender
     */
    @Autowired
    private JavaMailSender jms;

    /**
     * email的config
     */
    @Autowired
    private MyEmailMsgConfig emailMsgConfig;


    /**
     * 用户登录接口
     *
     * @param loginForm
     * @return
     */
    @ApiOperation(value = "登录", notes = "登录接口")
    @ApiImplicitParam(name = "loginForm", required = true, paramType = "body", value = "{\n" +
            "  \"account\": \"xiaopeng\",\n" +
            "  \"pwd\": \"121212\"\n" +
            "}")
    @PostMapping("/login")
    public JSONResult login(@RequestBody @Valid LoginForm loginForm) {
        try {
            BlogUser usr = userService.userLogin(loginForm);
            if (usr == null) { // 用户不存在
                return JSONResult.errorMsg("登录失败，用户不存在");
            } else {
                if (!(usr.getPwd().equals(loginForm.getPwd()))) { // 密码错误
                    return JSONResult.errorMsg("登录失败，密码错误");
                } else {
                    // 1.如果登录成功,设置redis(把token存入reids中),30分钟
                    String token = TOKEN.createJWT("JWT" + usr.getUserId(),
                            String.valueOf(usr.getUserId()), usr.getPwd());
                    REDIS.set(REDIS.USER_REDIS_SESSION + ":" + usr.getUserId(),
                            token,
                            30 * 60);
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


    /**
     * 用户注销
     *
     * @param reqData
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "用户注销", notes = "用户注销的接口")
    @ApiImplicitParam(name = "reqData", required = true, paramType = "body",
            value = "{\n" +
                    "\"userid\": \"1\"\n" +
                    "}")
    @PostMapping("/logout")
    @UserLoginToken
    public JSONResult logOut(@RequestBody(required = true) Map reqData,@ApiIgnore HttpServletRequest request) throws Exception {
        // 获取请求参数中的userid
        String userId = String.valueOf(reqData.get("userid"));
        // 获取token中的userid
        String token_userid = String.valueOf(request.getAttribute("current-user"));
        if (StringUtils.equals(userId, token_userid)) {
            logger.info("====>delset:" + REDIS.USER_REDIS_SESSION + ":" + userId);
            REDIS.del(REDIS.USER_REDIS_SESSION + ":" + userId);
            return JSONResult.ok();
        } else {
            return JSONResult.errorMsg("退出非法操作!");
        }
    }


    /**
     * 验证该nickname是否已经被占用
     *
     * @param reqData
     * @return
     */
    @ApiOperation(value = "用户验证用户nickname接口", notes = "用户验证接口")
    @ApiImplicitParam(name = "reqData", required = true,
            dataType = "String", paramType = "body", value = "{ \"nickname\": \"xiaopeng\" }")
    @PostMapping("/isaleadyexis")
    public JSONResult ifAleadyExists(@RequestBody(required = true) Map reqData) {
        String nickname = String.valueOf(reqData.get("nikcname"));
        if (!userService.findExistsUser(nickname)) {
            // 验证当前session内是否已有人
            String current = REDIS.get(REDIS.USER_REDIS_SESSION + ":" + REDIS.REGIST_SESSION + ":" + nickname);
            if (StringUtils.isEmpty(current)) {
                // 将此nickname存入session中(保存30分钟)
                REDIS.set(REDIS.USER_REDIS_SESSION + ":" + REDIS.REGIST_SESSION + ":" + nickname, nickname, 60 * 30);
                return JSONResult.ok();
            } else {
                return JSONResult.errorMsg("对不起该用户名被占用了");
            }
        } else {
            return JSONResult.errorMsg("对不起该用户名被占用了");
        }
    }


    /**
     * 发送邮箱验证码接口
     *
     * @param reqData
     * @return
     */
    @ApiOperation(value = "发送邮箱验证码接口", notes = "发送邮件，前提是需要通过nickname验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reqData", required = true, paramType = "body",
                    value = "{ \"touser\":\"2513943735@qq.com\", \"nickname\":\"xiaopeng\" }"),
    })
    @PostMapping("/sendemail")
    public JSONResult sendEmail(@RequestBody Map reqData) {
        String touser = String.valueOf(reqData.get("touser"));
        String nickname = String.valueOf(reqData.get("nickname"));
        String current = REDIS.get(REDIS.USER_REDIS_SESSION + ":" + REDIS.REGIST_SESSION + ":" + nickname);
        logger.info(REDIS.USER_REDIS_SESSION + ":" + REDIS.REGIST_SESSION + ":" + nickname);
        if (!StringUtils.isEmpty(current)) {
            // 建立邮件消息
            SimpleMailMessage mainMessage = new SimpleMailMessage();
            // 发送者
            mainMessage.setFrom(emailMsgConfig.getFrom());
            // 接收者
            mainMessage.setTo(touser);
            // 发送的标题
            mainMessage.setSubject(emailMsgConfig.getSubject1());
            // 发送的内容
            String code = SecurityCode.getSecutrityCode(5, SecurityCode.SecurityCodeLevel.Medium, false);

            REDIS.set(REDIS.USER_REDIS_SESSION + ":"
                    + REDIS.REGIST_SESSION + ":"
                    + nickname, code, 30 * 60);
            logger.info("code:" + code);
            String content = emailMsgConfig.getSubject1pre()
                    + code
                    + emailMsgConfig.getSubject2end();
            logger.info("发送的邮件内容：" + content);
            mainMessage.setText(content);
            jms.send(mainMessage);
            return JSONResult.ok("邮箱成功发送");
        } else {
            return JSONResult.errorMsg("对不起您没有权限!");
        }
    }


    /**
     * 添加账户接口
     *
     * @param userRegistForm
     * @return
     */
    @ApiOperation(value = "添加账户接口", notes = "用户注册")
    @ApiImplicitParam(name = "userRegistForm", required = true, paramType = "body",
            value = "{ \"nickname\": \"xiaopeng\", \"email\": \"dntchenpeng@12.com\", \"validatecode\": \"1212\", \"password\": \"121212\", \"confirmpass\": \"1212\" }")
    @PostMapping("/regist")
    public JSONResult addAccount(@RequestBody @Valid UserRegistForm userRegistForm) {
        BlogUser user = new BlogUser();
        user.setNickname(userRegistForm.getNickname());
        user.setEmail(userRegistForm.getEmail());
        String code = userRegistForm.getValidatecode();
        user.setPwd(userRegistForm.getPassword());
        // 验证code
        String validate_code = REDIS.get(REDIS.USER_REDIS_SESSION + ":"
                + REDIS.REGIST_SESSION + ":" + user.getNickname());
        if (StringUtils.isNotEmpty(validate_code) && StringUtils.equals(code, validate_code)) {
            // 添加用户
            if (userService.addBlogUser(user)) {
                // 注册成功后,应该把redis 里面保存的验证码的值删除
                REDIS.del(REDIS.USER_REDIS_SESSION + ":"
                        + REDIS.REGIST_SESSION + ":" + user.getNickname());
                return JSONResult.ok("注册成功！");
            } else {
                return JSONResult.errorMsg("对不起注册失败，请重试!");
            }
        } else {
            return JSONResult.errorMsg("对不起验证码输入错误!");
        }
    }


}
