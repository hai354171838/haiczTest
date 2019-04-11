package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticleSearchService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @author:Haicz
 * @date:2019/02/26
 * 搜索服务
 */
@RestController
@CrossOrigin//设置跨域
@RequestMapping("/search")
public class ArticleSearchController {

    @Autowired
    private ArticleSearchService articleSearchService;

    /**
     * 添加文章
     * @param article
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Article article){
        articleSearchService.add(article);
        return  new Result(true, StatusCode.OK,"增加成功");
    }

    /**
     * 搜索文章
     * @return
     */
    @RequestMapping(value = "/{keywords}/{page}/{size}",method = RequestMethod.GET)
    public Result search(@PathVariable String keywords,@PathVariable int page ,@PathVariable int size) {
        Page<Article> articleList= articleSearchService.search(keywords,page,size);
    	return new Result(true,StatusCode.OK,"查询成功",new PageResult<Article>(articleList.getTotalElements(),articleList.getContent()));
    }
}
