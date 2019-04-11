package com.tensquare.base.controller;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author:Haicz
 * @date:2019/02/01
 */
@CrossOrigin
@RestController
@RequestMapping("/label")
public class LabelController {
    @Autowired
    private LabelService labelService;

    /**
     * 查询所有
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
       // int i=1/0;
        return new Result(true,StatusCode.OK,"查询成功",labelService.findAll());
    }

    /**
     * 增加标签
     * @param label
     * @return
     */
    @RequestMapping(method =RequestMethod.POST)
    public Result add(@RequestBody Label label) {

        labelService.add(label);
        return  new Result(true,StatusCode.OK,"增加成功");

    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        System.out.println("base服务器~~");
        return new Result(true,StatusCode.OK,"查询成功",labelService.findById(id));
    }

    /**
     * 根据id更新
     * @param id
     * @param label
     * @return
     */
    @RequestMapping(value = "/{id}",method =RequestMethod.PUT)
    public Result update( String id,@RequestBody Label label) {
        label.setId(id);
        labelService.update(label);
        return new  Result(true,StatusCode.OK,"更新成功");
    }
    /**
     * 根据id删除标签
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public Result delete( @PathVariable String id) {
        labelService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 带条件搜索
     * @param label
     * @return
     */
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result findSearch(@RequestBody  Label label) {
        List<Label> list=labelService.findSearch(label);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }

    /**
     * 带条件分页查询
     * @param label
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result findSearch(@RequestBody  Label label,@PathVariable int page, @PathVariable int size) {
        Page<Label> pageList=labelService.findSearch(label,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult(pageList.getTotalElements(),pageList.getContent()));
    }


}
