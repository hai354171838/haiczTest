package com.tensquare.qa.client;

import com.tensquare.qa.client.impl.LabelClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author:Haicz
 * @date:2019/03/03
 */
@FeignClient(name="tensquare-base",fallback = LabelClientImpl.class)
public interface LabelClient {
    @RequestMapping(value = "/label/{id}", method = RequestMethod.GET)
    Result findById(@PathVariable(value = "id") String id);
}
