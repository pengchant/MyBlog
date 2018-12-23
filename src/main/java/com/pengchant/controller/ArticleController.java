package com.pengchant.controller;

import com.github.pagehelper.PageInfo;
import com.pengchant.form.ArticleDetailForm;
import com.pengchant.form.CommentsForm;
import com.pengchant.form.UserArticlesForm;
import com.pengchant.form.WriteArticleForm;
import com.pengchant.model.Article;
import com.pengchant.model.BlogUser;
import com.pengchant.model.Comment;
import com.pengchant.service.IArticleService;
import com.pengchant.service.IUserService;
import com.pengchant.utils.JSONResult;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
@Api(value = "文章相关接口", tags = {"执行文章需求"})
public class ArticleController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private IArticleService articleService;

    @Autowired
    private IUserService userService;

    /**
     * 编写文章
     * @param articleForm
     * @return
     */
    @ApiOperation(value = "编写文章接口", notes = "编写文章")
    @ApiImplicitParam(name = "articleForm", value = "{ \"title\": \"idea的用法\", \"content\": \"HELLOWORLD\", \"suber\": \"13\" }", paramType = "body")
    @PostMapping("/write")
    public JSONResult write(@RequestBody @Valid WriteArticleForm articleForm) {
        Article myarticle = new Article();
        myarticle.setTitle(articleForm.getTitle());
        myarticle.setContent(articleForm.getContent());
        myarticle.setSuber(Integer.valueOf(articleForm.getSuber()));
        myarticle.setSubtime(new Date(System.currentTimeMillis()));
        myarticle.setDescribtion(articleForm.getDescrib());
        myarticle.setGreated(0);
        myarticle.setShared(0);
        myarticle.setViewed(0);
        myarticle.setSts((byte) 1);
        if (articleService.writeArticle(myarticle)) {
            // 回显用户信息
            BlogUser result = userService.findUserById(Integer.valueOf(myarticle.getSuber()));
            result.setPwd("逗比，没有密码别看了");
            return JSONResult.ok(result);
        } else {
            return JSONResult.errorMsg("插入失败，请重试！");
        }
    }

    /**
     * 获取我的文章列表
     *
     * @param reqData
     * @return
     */
    @ApiModelProperty(name = "获取我的所有的文章", notes = "获取文章列表")
    @ApiImplicitParam(name = "reqData", value = "{\n" +
            "\"userid\": \"12\",\n" +
            "\"pageno\": \"1\",\n" +
            "\"pagesize\": \"3\"\n" +
            "}", paramType = "body", required = true)
    @PostMapping("/allmyarticle")
    public JSONResult getAllMyArticle(@RequestBody(required = true) Map reqData) {
        try {
            String pageno = String.valueOf(reqData.get("pageno"));
            String pagesize = String.valueOf(reqData.get("pagesize"));
            String userid = String.valueOf(reqData.get("userid"));
            if (StringUtils.isEmpty(pageno) ||
                    StringUtils.isEmpty(pagesize) ||
                    StringUtils.isEmpty(userid)) {
                return JSONResult.errorMsg("请将字段输入完整!");
            } else {
                PageInfo<Article> result = articleService.queryMyArticles(userid, Integer.valueOf(pageno),
                        Integer.valueOf(pagesize));
                return JSONResult.ok(result);
            }
        } catch (NumberFormatException e) {
            return JSONResult.errorMsg("字段非法，请检查pageNo, pageSize是否为数字.");
        }
    }


    /**
     * 获取文章的细节
     *
     * @param reqData
     * @return
     */
    @ApiModelProperty(name = "获取文章的细节", value = "获取文章详情")
    @ApiImplicitParam(name = "reqData", value = "文章编号idjson包", required = true, paramType = "body")
    @PostMapping("/articledetail")
    public JSONResult artidetail(@RequestBody(required = true) Map reqData) {
        String articleId = String.valueOf(reqData.get("articleid"));
        if (StringUtils.isNotEmpty(articleId)) {
            ArticleDetailForm result = articleService.queryArticleForm(articleId);
            return JSONResult.ok(result);
        } else {
            return JSONResult.errorMsg("编号不能为空");
        }
    }

    /**
     * 提交评论
     *
     * @param comment
     * @return
     */
    @ApiModelProperty(name = "提交评论", value = "提交评论")
    @ApiImplicitParam(name = "comment", value = "评论的表单", required = true)
    @PostMapping("/addcomm")
    public JSONResult addCommon(@RequestBody(required = true) CommentsForm comment) {
        Comment comm = new Comment();
        comm.setArticleid(Integer.valueOf(comment.getArticleid()));
        comm.setContent(comment.getComments());
        comm.setSuberid(Integer.valueOf(comment.getSuberid()));
        comm.setSuber(comment.getSubername());
        comm.setSubtime(new Date());
        // 添加数据
        if (articleService.addComments(comm)) {
            return JSONResult.ok("评论成功");
        }
        return JSONResult.errorMsg("评论失败，请重试！");
    }

    /**
     * 获取某个文章的所有评论
     * @param arcid
     * @return
     */
    @ApiModelProperty(name = "获取某个文章的所有评论", value = "获取评论")
    @ApiImplicitParam(name = "arcid", value = "文章编号", required = true, paramType = "path")
    @PostMapping("/allcomments/{arcid}")
    public JSONResult queryAllComments(@PathVariable("arcid") String arcid){
        List<CommentsForm> results = articleService.queryAllComments(arcid);
        return JSONResult.ok(results);
    }

    /**
     * 获取所有的文章列表
     *
     * @param reqData
     * @return
     */
    @ApiModelProperty(name = "查询所有文章", value = "文章列表")
    @ApiImplicitParam(name = "reqData", value = "pageno, pagesize", required = true, paramType = "body")
    @PostMapping("/all")
    public JSONResult queryAllArticles(@RequestBody(required = true) Map reqData) {
        try {
            int pageno = Integer.valueOf(String.valueOf(reqData.get("pageno")));
            int pagesize = Integer.valueOf(String.valueOf(reqData.get("pagesize")));
            PageInfo<UserArticlesForm> results = articleService.queryAllUserArticles(pageno, pagesize);
            return JSONResult.ok(results);
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.errorMsg("提交字段有误!");
        }
    }

    /**
     * 增加阅读量
     *
     * @param arcid
     * @return
     */
    @ApiModelProperty(name = "增加阅读次数", value = "增加阅读次数")
    @ApiImplicitParam(name = "arcid", value = "文章编号", required = true, paramType = "path")
    @PostMapping("/increase/{arcid}")
    public JSONResult increaseViewed(@PathVariable String arcid) {
        if (articleService.increaseViewed(Integer.parseInt(arcid))) {
            return JSONResult.ok("修改成功！");
        }
        return JSONResult.errorMsg("对不起操作失败!");
    }

    /**
     * 删除文章
     *
     * @param arcid
     * @param suberid
     * @return
     */

    @ApiModelProperty(name = "删除文章", value = "删除文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "arcid", value = "文章编号", required = true, paramType = "path"),
            @ApiImplicitParam(name = "suberid", value = "用户的编号", required = true, paramType = "path")
    })
    @PostMapping("/del/{arcid}/{suberid}")
    public JSONResult delArcid(@PathVariable int arcid, @PathVariable int suberid) {
        if (articleService.deleteArticle(arcid, suberid)) {
            // 回显用户信息
            BlogUser result = userService.findUserById(Integer.valueOf(suberid));
            result.setPwd("逗比，没有密码别看了");
            return JSONResult.ok(result);
        }
        return JSONResult.errorMsg("删除文章失败!");
    }




}
