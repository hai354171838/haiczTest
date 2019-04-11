package com.tensquare.foreground.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author:Haicz
 * @date:2019/03/08
 */
@Component
public class WebFilter extends ZuulFilter {

    @Override
    //配置过滤的类型, "pre": 路由, 转发到微服务之前
    public String filterType() {
        return "pre";
    }

    @Override
    //配置优先级的, 值越少, 优先级越高
    public int filterOrder() {
        return 0;
    }

    @Override
    //配置是否会经过这个拦截器; true 会经过(通过run方法来处理); false: 不会经过
    public boolean shouldFilter() {
        return true;
    }

    @Override
    //处理逻辑的方法; 只有当shouldFilter返回true才执行
    //客户端请求微服务,有些需要携带token, token认证头(Authorization)经过过滤器会丢失的. 都是我们需要在后面某个/些 微服务里面需要获得token,
    //解决: 先在过滤器里面取出来, 再设置一次
    public Object run() throws ZuulException {
        System.out.println("WebFilter 收到了请求...");
        //1.获得请求的上下文, 获得request
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        //2.获得Authorization这个请求头
        String authorization = request.getHeader("Authorization");
        //3.判断, 如果有, 再设置进去
        if (authorization!=null){
            context.addOriginResponseHeader("Authorization",authorization );
        }


        return null;
    }
}
