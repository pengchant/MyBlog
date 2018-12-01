package com.pengchant.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pengchant.configure.MyTokenConfig;
import com.pengchant.model.BlogUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * token的工具类
 */
@Component
public class MyToken {

    @Autowired
    MyTokenConfig myTokenConfig;

    /**
     * token生成方法
     *
     * @param user
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getToken(BlogUser user) {
        String token = null;
        try {
            token = "";
            token = JWT.create()
                    .withAudience(String.valueOf(user.getUserId()))
                    .sign(Algorithm.HMAC256(user.getPwd()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return token;
    }


    /**
     * 创建JWT
     * @param id    jwt的唯一标志
     * @param subject   客户端信息
     * @return
     * @throws Exception
     */
    public String createJWT(String id, String subject, String salt) throws Exception {
        //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //创建payload的私有声明
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("uid", "XUZHOU_ZHENGJIANG_NANJING");
        claims.put("user_name", "ADMINNN");
        claims.put("nick_name", "XIAOPENGYAONIXI");

        //生成签名的时候使用的秘钥secret
        SecretKey key = generalKey(salt);

        //下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() //这里其实就是new一个JwtBuilder
                .setClaims(claims)          //设置这个自己创建的私有的声明
                .setId(id)                  //设置jti(JWT ID)：是JWT的唯一标识
                .setIssuedAt(now)           //iat: jwt的签发时间
                .setSubject(subject)        //sub(Subject)：代表这个JWT的主体作为什么用户的唯一标志。
                .signWith(signatureAlgorithm, key);//设置签名使用的签名算法和签名使用的秘钥

        //设置过期时间
        if (myTokenConfig.getTtlmillis() >= 0) {
            long expMillis = nowMillis + myTokenConfig.getTtlmillis();
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public Claims parseJWT(String jwt, String salt) throws Exception {
        SecretKey key = generalKey(salt);  //签名秘钥，和生成的签名的秘钥一模一样
        Claims claims = Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(key)         //设置签名的秘钥
                .parseClaimsJws(jwt).getBody();//设置需要解析的jwt
        return claims;
    }

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public SecretKey generalKey(String salt) {
        String stringKey = myTokenConfig.getAppkey() + salt;
        byte[] encodedKey = Base64.decodeBase64(stringKey);//本地的密码解码
        SecretKey key = new SecretKeySpec(encodedKey,
                0, encodedKey.length,
                "AES");// 根据给定的字节数组使用AES加密算法构造一个密钥
        return key;
    }


    public static void main(String[] args) throws Exception {
        MyToken util = new MyToken();
        util.myTokenConfig = new MyTokenConfig();
        util.myTokenConfig.setAppkey("fdsafa");
        util.myTokenConfig.setTtlmillis(300000);

        String ab = util.createJWT("jwtid", "userid",  "userpassword");
        System.out.println(ab);

        String userId;
        userId = JWT.decode(ab).getSubject().toString();

        System.out.println("userid:"+ userId);

//        Claims c=util.parseJWT(ab, "userpassword");//注意：如果jwt已经过期了，这里会抛出jwt过期异常。
//        System.out.println(c.getId());//jwt
//        System.out.println(c.getIssuedAt());// 发布时间
//        System.out.println(c.getSubject());// 表示一个客户端
//        System.out.println(c.getIssuer());//null
//        System.out.println(c.get("uid", String.class));


    }


}
