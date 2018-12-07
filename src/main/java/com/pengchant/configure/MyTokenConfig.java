package com.pengchant.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "myemail")
public class MyTokenConfig {

    /**
     * 密钥
     */
    private String appkey = "";

    /**
     * token刷新周期
     */
    private long ttlmillis = 60000l;


    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public long getTtlmillis() {
        return ttlmillis;
    }

    public void setTtlmillis(long ttlmillis) {
        this.ttlmillis = ttlmillis;
    }
}
