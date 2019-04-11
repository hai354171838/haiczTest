package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author:Haicz
 * @date:2019/03/03
 */
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;
    /**
     * 添加朋友或者不喜欢好友
     * @return
     */
    @RequestMapping(value = "/like/{friendid}/{type}",method = RequestMethod.PUT)
    public Result addFriend(@PathVariable String friendid, @PathVariable String type, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims==null){
            return new Result(false,StatusCode.ACCESSERROR,"权限不足");
        }
        //调用业务,添加好友
        if ("1".equals(type)){
            //添加好友
            int  result=friendService.addFriend(claims.getId(),friendid);
            if (result==0){
                return new Result(true, StatusCode.ERROR, "该好友已经添加过", null);
            }
        }else {
            //不喜欢好友
            friendService.addNoFriend(claims.getId(),friendid);
        }

        return new Result(true,StatusCode.OK,"添加成功");
    }

    /**
     * 删除好友
     * @param friendid
     * @param request
     * @return
     */
    @RequestMapping(value = "/{friendid}",method = RequestMethod.DELETE)
    public Result deleteFriend(@PathVariable String friendid,HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims==null){
            return new Result(false,StatusCode.ACCESSERROR,"权限不足");
        }
        friendService.deleteFriend(claims.getId(),friendid);
    	return new Result(true,StatusCode.OK,"删除成功");
    }


}
