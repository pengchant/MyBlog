package com.pengchant.controller;

import com.alibaba.fastjson.parser.JSONReaderScanner;
import com.pengchant.annotations.UserLoginToken;
import com.pengchant.configure.MyEmailMsgConfig;
import com.pengchant.form.LoginForm;
import com.pengchant.form.MyCaredUser;
import com.pengchant.form.MyFansUser;
import com.pengchant.form.UserRegistForm;
import com.pengchant.model.BlogUser;
import com.pengchant.service.IUserService;
import com.pengchant.utils.JSONResult;
import com.pengchant.utils.MyToken;
import com.pengchant.utils.RedisOperator;
import com.pengchant.utils.SecurityCode;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Api(value = "用户相关接口", tags = {"执行用户信息需求"})
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    private final ResourceLoader resourceLoader;

    @Autowired
    public UserController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

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
    public JSONResult login(@Valid @RequestBody LoginForm loginForm) {
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
    public JSONResult logOut(@RequestBody(required = true) Map reqData, @ApiIgnore HttpServletRequest request) throws Exception {
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
     * 验证该nickname/email是否已经被占用
     *
     * @param reqData
     * @return
     */
    @ApiOperation(value = "用户验证用户nickname/email接口", notes = "用户验证接口")
    @ApiImplicitParam(name = "reqData", required = true,
            dataType = "String", paramType = "body", value = "{ \"searchstr\": \"xiaopeng\" }")
    @PostMapping("/isaleadyexis")
    public JSONResult ifAleadyExists(@RequestBody(required = true) Map reqData) {
        String searchstr = String.valueOf(reqData.get("searchstr"));
        if (!userService.findExistsUser(searchstr)) {
            return JSONResult.ok();
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
                    value = "{ \"touser\":\"2513943735@qq.com \"}"),
    })
    @PostMapping("/sendemail")
    public JSONResult sendEmail(@RequestBody Map reqData) {
        String touser = String.valueOf(reqData.get("touser"));
        logger.info(REDIS.USER_REDIS_SESSION + ":" + REDIS.REGIST_SESSION + ":" + touser);
        if (!StringUtils.isEmpty(touser)) {
            // 建立邮件消息
            SimpleMailMessage mainMessage = new SimpleMailMessage();
            // 发送者
            mainMessage.setFrom(emailMsgConfig.getFrom());
            // 接收者
            mainMessage.setTo(touser);
            // 发送的标题
            mainMessage.setSubject(emailMsgConfig.getSubject1());
            // 发送的内容
            String code = SecurityCode.getSecutrityCode(5,
                    SecurityCode.SecurityCodeLevel.Medium,
                    false);
            REDIS.set(REDIS.USER_REDIS_SESSION + ":"
                    + REDIS.REGIST_SESSION + ":"
                    + touser, code, 30 * 60);
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
    public JSONResult addAccount(@Valid @RequestBody UserRegistForm userRegistForm) {
        BlogUser user = new BlogUser();
        user.setNickname(userRegistForm.getNickname());
        user.setEmail(userRegistForm.getEmail());
        String code = userRegistForm.getValidatecode();
        user.setPwd(userRegistForm.getPassword());
        // 验证code
        String validate_code = REDIS.get(REDIS.USER_REDIS_SESSION + ":"
                + REDIS.REGIST_SESSION + ":" + user.getEmail());
        if (StringUtils.isNotEmpty(validate_code) && StringUtils.equals(code, validate_code)) {
            // 添加用户
            if (userService.addBlogUser(user)) {
                // 注册成功后,应该把redis 里面保存的验证码的值删除
                REDIS.del(REDIS.USER_REDIS_SESSION + ":"
                        + REDIS.REGIST_SESSION + ":" + user.getEmail());
                return JSONResult.ok("注册成功！");
            } else {
                return JSONResult.errorMsg("对不起注册失败，请重试!");
            }
        } else {
            return JSONResult.errorMsg("对不起验证码输入错误!");
        }
    }


    /**
     * 配置图片上传的路径
     */
    @Value("${img.location}")
    private String location;

    @ApiModelProperty(name = "用户上传头像", value = "上传头像")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "usrid", value = "用户编号", paramType = "path", required = true)
            }
    )
    @PostMapping("/uploadimg/{usrid}")
    public JSONResult uploadHeaderimg(@RequestParam("userheaderimg")MultipartFile usrimg,@PathVariable("usrid") int usrid){
        if(usrimg.isEmpty() || StringUtils.isBlank(usrimg.getOriginalFilename())){
            return JSONResult.errorMsg("对不起上传图片不能为空，且图片文件名称不能为空");
        }
        String contentType = usrimg.getContentType();
        if(!contentType.contains("jpeg")){
            return JSONResult.errorMsg("上传的图片格式不对,请上传jpg格式的图片");
        }
        String root_filename = usrimg.getOriginalFilename();
        logger.info("上传到图片的文件名为:{},类型为{}", root_filename, contentType);

        try {
            // 获取文件后缀
            String suffix = root_filename.substring(root_filename.lastIndexOf("."));
            // 生成新的文件名
            String new_filename = UUID.randomUUID().toString().replace("-","")+".jpg";

            // 保存文件的相对位置除了父级目录(保存到数据库中)
            String relative_path = "/" + usrid+"/" + new_filename;
            // 主目录(实际保存的位置)
            String real_path = this.location + relative_path;
            File dest = new File(real_path);
            // 判断父目录是否存在
            if(!dest.getParentFile().exists()){
                dest.getParentFile().mkdirs();
            }
            // 完成保存操作
            usrimg.transferTo(dest);

            // ******删除之前的图片*****
            BlogUser _theuser  = userService.findUserById(usrid);
            if(_theuser!=null){
                String _rel_path = _theuser.getHeadimgurl();
                File _delFile = new File(location+_rel_path);
                if(_delFile.exists()&&_delFile.isFile()){
                    _delFile.delete();
                }
            }
            // ******删除之前的图片*****

            // 将相对路径保存到数据库中
            BlogUser user = userService.saveUserImgUrl(usrid, relative_path);
            user.setPwd("");
            return JSONResult.ok(user);
        } catch (IOException e) {
            logger.info("异常");
        }
        return JSONResult.errorMsg("对不起，上传失败请稍后重试！");
    }


    /**
     * 回显图片
     * @param fileName
     * @return
     */
    @ApiModelProperty(name = "显示图片", value = "显示图片")
    @ApiImplicitParam(name = "fileName", value = "文件相对路径",paramType = "string", required = true )
    @GetMapping("/show")
    public ResponseEntity showPhotos(String fileName){
        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + location + fileName));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取用户信息
     * @param userid
     * @return
     */
    @ApiModelProperty(name = "获取用户信息", value = "用户信息")
    @ApiImplicitParam(value ="用户编号", name = "userid", required = true, paramType = "path")
    @PostMapping("/queryuser/{userid}")
    public JSONResult queryUserInfo(@PathVariable("userid") int userid){
        BlogUser user = userService.findUserById(userid);
        if(user!=null){
            user.setPwd("");
        }
        return JSONResult.ok(user);
    }

    /**
     * 查询我的所有关注
     * @param myid
     * @return
     */
    @ApiModelProperty(value = "我的所有关注", name = "allmycared")
    @ApiImplicitParam(name = "myid", value = "我的编号", required = true, paramType = "path")
    @PostMapping("/allmycared/{myid}")
    public JSONResult allMyCared(@PathVariable("myid") String myid){
        List<MyCaredUser> result = userService.allMyCared(myid);
        return JSONResult.ok(result);
    }

    /**
     * 取消关注
     * @param reqData
     * @return
     */
    @ApiModelProperty(value = "取消关注", name = "cancelcared", notes = "取消关注的接口")
    @ApiImplicitParam(name = "reqData", value = "请求数据", paramType = "path", required = true)
    @PostMapping("/cancelcared")
    public JSONResult calcelCared(@RequestBody Map reqData){
        String myid = String.valueOf(reqData.get("myid"));
        String blgid = String.valueOf(reqData.get("blgid"));
        if(StringUtils.isNotBlank(myid)&&StringUtils.isNotBlank(blgid)){
            if(userService.cancelCared(myid, blgid)){
                // 查询当前用户信息
                BlogUser current = userService.findUserById(Integer.valueOf(myid));
                current.setPwd("逗比，别看了没有密码");
                return JSONResult.ok(current);
            }
        }
        return JSONResult.errorMsg("操作失败!");
    }


    /**
     * 查询我的粉丝
     * @param myid
     * @return
     */
    @ApiModelProperty(value = "我的粉丝", name = "allmyfans")
    @ApiImplicitParam(name = "myid", value = "我的编号", required = true, paramType = "path")
    @PostMapping("/allmyfans/{myid}")
    public JSONResult allMyFans(@PathVariable("myid") String myid){
        List<MyFansUser> result = userService.allMyFans(myid);
        return JSONResult.ok(result);
    }


    /**
     * 添加关注
     * @param reqData
     * @return
     */
    @ApiModelProperty(value = "添加关注", name = "addcared", notes = "添加关注")
    @ApiImplicitParam(name = "reqData", value = "请求数据", paramType = "body", required = true)
    @PostMapping("/addcared")
    public JSONResult addCared(@RequestBody Map reqData){
        String myid = String.valueOf(reqData.get("myid"));
        String blgid = String.valueOf(reqData.get("blgid"));
        if(StringUtils.isNotBlank(myid)&&StringUtils.isNotBlank(blgid)){
            if(userService.addCared(blgid, myid)){
                // 查询当前用户信息
                BlogUser current = userService.findUserById(Integer.valueOf(myid));
                current.setPwd("逗比，别看了没有密码");
                return JSONResult.ok(current);
            }
        }
        return JSONResult.errorMsg("操作失败!");
    }


    /**
     * 是否已经关注
     * @param reqData
     * @return
     */
    @ApiModelProperty(value = "是否已经关注", name = "hascared", notes = "是否已经关注")
    @ApiImplicitParam(name = "reqData", value = "请求数据", paramType = "body", required = true)
    @PostMapping("/hascared")
    public JSONResult hasCared(@RequestBody Map reqData){
        String myid = String.valueOf(reqData.get("myid"));
        String blgid = String.valueOf(reqData.get("blgid"));
        if(StringUtils.isNotBlank(myid)&&StringUtils.isNotBlank(blgid)){
            if(userService.hasCared(myid, blgid)){
                return JSONResult.ok(true);
            }else{
                return JSONResult.ok(false);
            }
        }
        return JSONResult.errorMsg("操作失败!");
    }


}
