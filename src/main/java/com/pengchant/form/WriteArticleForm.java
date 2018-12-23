package com.pengchant.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(description = "编写文章表单", value = "编写文章")
public class WriteArticleForm {

    /**
     * 文章标题
     */
    @ApiModelProperty(name = "文章标题")
    @NotBlank(message = "对不起文章的标题不能为空")
    private String title;

    @ApiModelProperty(name = "文章描述")
    @NotBlank(message = "文章描述不能为空")
    private String describ;

    /**
     * 内容
     */
    @ApiModelProperty(name = "内容")
    @NotBlank(message = "对不起文章内容不能为空")
    private String content;

    /**
     * 编写人
     */
    @ApiModelProperty(name = "编写人")
    @NotBlank(message = "编写人不能为空")
    private String suber;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSuber() {
        return suber;
    }

    public void setSuber(String suber) {
        this.suber = suber;
    }

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ;
    }
}
