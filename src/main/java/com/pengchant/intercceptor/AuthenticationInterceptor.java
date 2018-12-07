package com.pengchant.intercceptor;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.pengchant.annotations.PassToken;
import com.pengchant.annotations.UserLoginToken;
import com.pengchant.model.BlogUser;
import com.pengchant.service.IUserService;
import com.pengchant.utils.JSONResult;
import com.pengchant.utils.MyToken;
import com.pengchant.utils.RedisOperator;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * 获取token并验证token的拦截器
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private MyToken TOKEN;

    @Autowired
    private RedisOperator REDIS;

    /**
     * 拦截请求
     *
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");

        // 如果不是映射到方法跳过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 检查是否有passtoken注释如果有就跳过验证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }

        // 检查是否有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                try {
                    //  1.查看token是否为空
                    if (token == null) {
                        showError(request, response, "TOKEN验证失败,请重新登录!");
                        return false;
                    }

                    // 2.获取其中的userid
                    String userId;
                    userId = JWT.decode(token).getSubject().toString();

                    // 3.查看redis中是否存在该用户的登录信息
                    String usr_session = REDIS.get(REDIS.USER_REDIS_SESSION + ":" + userId);
                    if (StringUtils.isEmpty(usr_session)) {// 提示重新登录
                        showError(request, response, "登录过期，请重新登录");
                        return false;
                    }

                    // 4.验证是否被篡改
                    if(!StringUtils.equals(usr_session, token)){
                        showError(request, response, "token非法，请重新登录!");
                        return false;
                    }

                    // 5.查询用户的信息
                    BlogUser user = userService.findUserById(Integer.valueOf(userId));
                    if (user == null) {
                        showError(request, response, "此用户不存在，请重新登录!");
                        return false;
                    }

                    // 6.验证token是否有效（过期）
                    try {
                        Claims claims = TOKEN.parseJWT(token, user.getPwd());
                        logger.info("验证通过，当前的用户为:" + user.getUserId());
                        // 把request设置在请求头中
                        request.setAttribute("current-user", user.getUserId());
                    } catch (Exception e) {
                        // 过期异常，重新生成新的token
                        e.printStackTrace();
                        logger.info("token过期异常，重新生成新的token",e.getMessage());
                        showError(request, response, "token过期异常，重新生成新的token");
                        return false;
                    }
                } catch (Exception e) {
                    showError(request, response, "对不起,系统繁忙请稍后重试...");
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    /**
     * 整个请求处理完毕回调方法
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
         request.removeAttribute("current-user");
    }

    /**
     * 设置返回的内容
     *
     * @param request
     * @param response
     * @param msg
     */
    private void showError(HttpServletRequest request, HttpServletResponse response, String msg) {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = null;
        try {
            msg = JSON.toJSONString(JSONResult.errorTokenMsg(msg));
            out = response.getWriter();
            out.print(msg);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
}
