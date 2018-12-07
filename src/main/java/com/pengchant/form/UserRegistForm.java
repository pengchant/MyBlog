package com.pengchant.form;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 用户注册表单
 */
@ApiModel(value = "用户对象", description = "用户实体")
public class UserRegistForm {

    /**
     * 用户名
     */
    @ApiModelProperty(name = "用户名")
    @NotNull(message = "昵称不能为空")
    private String nickname;

    /**
     * 用户邮箱
     */
    @ApiModelProperty(name = "用户邮箱")
    @NotNull(message = "邮箱不能为空")
    @Email(message = "请输入合法的邮箱")
    private String email;

    /**
     * 验证码
     */
    @ApiModelProperty(name = "用户名")
    @NotNull(message = "验证码不能为空")
    private String validatecode;

    /**
     * 用户密码
     */
    @ApiModelProperty(name = "用户密码")
    @NotNull(message = "用户密码不能为空")
    @Size(min = 6, max = 20, message = "对不起密码长度必须大于等于6小于等于20，请重新修改")
    private String password;

    /**
     * 确认密码
     */
    @ApiModelProperty(name = "确认密码")
    @NotNull(message = "用户密码不能为空")
    @Size(min = 6, max = 20, message = "对不起密码长度必须大于等于6小于等于20，请重新修改")
    private String confirmpass;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getValidatecode() {
        return validatecode;
    }

    public void setValidatecode(String validatecode) {
        this.validatecode = validatecode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpass() {
        return confirmpass;
    }

    public void setConfirmpass(String confirmpass) {
        this.confirmpass = confirmpass;
    }


    public static void main(String[] args) {
        UserRegistForm u = new UserRegistForm();
        System.out.println(JSON.toJSONString(u));
    }
}


