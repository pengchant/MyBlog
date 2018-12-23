package com.pengchant.form;

import io.swagger.annotations.ApiModel;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户登录表单
 */
@ApiModel(description = "用户登录表单" , value = "登录表单")
public class LoginForm {

    @NotBlank(message = "账户不能为空")
    private String account;

    @NotBlank(message = "密码不能为空")
    private String pwd;

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAccount() {
        return account;
    }

    public String getPwd() {
        return pwd;
    }
}
