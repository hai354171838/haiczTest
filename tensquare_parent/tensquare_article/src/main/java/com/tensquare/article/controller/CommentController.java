package com.tensquare.article.controller;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.service.CommentService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author:Haicz
 * @date:2019/02/25
 */
@RestController
@CrossOrigin
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;


    /**
     * 查询全部数据
     * @return
     */
    @RequestMapping(method= RequestMethod.GET)
    public Result findAll(){
        return new Result(true,StatusCode.OK,"查询成功",commentService.findAll());
    }

    /**
     * 添加评论
     * @param comment
     * @return
     */
    @RequestMapping(method= RequestMethod.POST)
    public Result save(@RequestBody Comment comment, HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims==null){
            return new Result(false,StatusCode.ACCESSERROR,"权限不足");
        }
        comment.setUserid(claims.getId());
        commentService.add(comment);
        return new Result(true, StatusCode.OK, "提交成功 ");
    }

    /**
     * 根据文章ID查询评论
     * @param articleid
     * @return
     */
    @RequestMapping(value = "/article/{articleid}",method = RequestMethod.GET)
    public Result findByArticleid(@PathVariable String articleid) {
        List<Comment> list=commentService.findByArticleid(articleid);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }
    /**
     * 删除评论
     * @param id
     */
    @RequestMapping(value="/{id}",method= RequestMethod.DELETE)
    public Result delete(@PathVariable String id ){
        commentService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

}
