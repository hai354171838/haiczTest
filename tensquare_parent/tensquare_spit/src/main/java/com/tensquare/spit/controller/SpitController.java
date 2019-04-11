package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author:Haicz
 * @date:2019/02/25
 */
@RestController
@RequestMapping("/spit")
public class SpitController {

    @Autowired
    private SpitService spitService;
    @Autowired
    private RedisTemplate redisTemplate;



    /**
     * 查询所有
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        List<Spit>spitList= spitService.findAll();

        return new Result(true,StatusCode.OK,"查询成功",spitList);
    }

    /**
     * 根据id查询吐槽
     * @return
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId) {
        Spit spit= spitService.findById(spitId);
    	return new Result(true,StatusCode.OK,"查询成功",spit);
    }

    /**
     * 增加吐槽
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Spit spit) {
        spitService.add(spit);
    	return new Result(true,StatusCode.OK,"添加成功");
    }

    /**
     * 修改吐槽
     * @return
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.PUT)
    public Result update(@PathVariable String spitId,@RequestBody Spit spit) {
        spit.set_id(spitId);
        spitService.update(spit);
    	return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 根据id删除吐槽
     * @return
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String spitId) {
        spitService.delete(spitId);
    	return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 吐槽点赞
     * @param spitId
     * @return
     */
    @RequestMapping(value = "/thumbup/{spitId}",method = RequestMethod.PUT)
    public Result updateThumbup(@PathVariable String spitId) {
        //判断用户是否点过赞
        String userid="2023";// 后边我们会修改为当前登陆的用户
        if (redisTemplate.opsForValue().get("thumbup_"+userid+"_"+spitId)!=null){
            return new Result(false,StatusCode.REPERROR,"你已经点过赞了");
        }
        spitService.updateThumbup(spitId);
        redisTemplate.opsForValue().set("thumbup_"+userid+"_"+spitId,"1");
    	return new Result(true,StatusCode.OK,"点赞成功");
    }
    /**
     * 带条件搜索
     * @param spit
     * @return
     */
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Spit spit) {
        List<Spit> list=spitService.findSearch(spit);

        return new Result(true,StatusCode.OK,"查询成功",list);
    }

    /**
     * 带条件+分页搜索
     * @param spit
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.GET)
    public Result findSearch(@RequestBody Spit spit,@PathVariable int page,@PathVariable int size) {
        Page<Spit> list=spitService.findSearch(spit,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Spit>(list.getTotalElements(),list.getContent()));
    }

    /**
     *
     * 根据上级ID查询吐槽数据（分页）
     * @return
     */
    @RequestMapping(value = "/comment/{parentid}/{page}/{size}",method = RequestMethod.GET)
    public Result findByParentid(@PathVariable String parentid,@PathVariable int page,@PathVariable int size) {
        Page<Spit> pageList= (Page<Spit>) spitService.findByParentid(parentid,page,size);
    	return new Result(true,StatusCode.OK,"查询成功",new PageResult<Spit>(pageList.getTotalElements(),pageList.getContent()));
    }
    /**
     * 吐槽增加浏览
     * @param spitId
     * @return
     */
    @RequestMapping(value = "/visits/{spitId}",method = RequestMethod.PUT)
    public Result updateVisits(@PathVariable String spitId) {
        //判断用户是浏览过
        String userid="2023";// 后边我们会修改为当前登陆的用户
        if (redisTemplate.opsForValue().get("visits_"+userid+"_"+spitId)!=null){
            return new Result(false,StatusCode.REPERROR,"你已经浏览过了");
        }
        spitService.updateVisits(spitId);
        redisTemplate.opsForValue().set("visits_"+userid+"_"+spitId,"1",1,TimeUnit.DAYS);
        return new Result(true,StatusCode.OK,"浏览成功");
    }

    /**
     * 吐槽增加分享量
     * @param spitId
     * @return
     */
    @RequestMapping(value = "/share/{spitId}",method = RequestMethod.PUT)
    public Result updateShare(@PathVariable String spitId) {
        //判断用户是否点过赞
        String userid="2023";// 后边我们会修改为当前登陆的用户
        if (redisTemplate.opsForValue().get("share_"+userid+"_"+spitId)!=null){
            return new Result(false,StatusCode.REPERROR,"你已分享过了");
        }
        spitService.updateShare(spitId);
        redisTemplate.opsForValue().set("share_"+userid+"_"+spitId,"1",1,TimeUnit.DAYS);
        return new Result(true,StatusCode.OK,"分享成功");
    }

}
