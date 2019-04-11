package com.tensquare.friend.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author:Haicz
 * @date:2019/03/06
 */
@FeignClient("tensquare-user")
public interface UserClient {

    /**
     * 更新粉丝数
     * @param userId user/incFans/{userId}/{count}
     * @param count
     * @return
     */
    @RequestMapping(value = "user/incFans/{userId}/{count}",method = RequestMethod.PUT)
    Result incFanscount(@PathVariable("userId") String userId, @PathVariable("count") int count);

    /**
     * 更新关注数
     * @param userId /incFollowcount/{userId}/{count}
     * @param count
     * @return
     */
    @RequestMapping(value = "user/incFollow/{userId}/{count}",method = RequestMethod.PUT)
    Result incFollowcount(@PathVariable("userId") String userId, @PathVariable("count") int count);
}
