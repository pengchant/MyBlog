package com.pengchant.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 我的邮箱配置类
 */
@Component
@ConfigurationProperties(prefix = "myemail")
public class MyEmailMsgConfig {

    private String from;

    private String subject1;

    private String subject1pre;

    private String subject2end;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject1() {
        return subject1;
    }

    public void setSubject1(String subject1) {
        this.subject1 = subject1;
    }

    public String getSubject1pre() {
        return subject1pre;
    }

    public void setSubject1pre(String subject1pre) {
        this.subject1pre = subject1pre;
    }

    public String getSubject2end() {
        return subject2end;
    }

    public void setSubject2end(String subject2end) {
        this.subject2end = subject2end;
    }
}
