package com.tensquare.manager.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author:Haicz
 * @date:2019/03/08
 */
@Component
public class WebFilter extends ZuulFilter {
    @Autowired
    private JwtUtil jwtUtil;
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
        try {
            System.out.println("WebFilter 收到了请求...");
            //1.获得请求的上下文, 获得request
            RequestContext context = RequestContext.getCurrentContext();
            HttpServletRequest request = context.getRequest();
            //判断是否是跨域(如果做跨域请求时候，浏览器会自动发起一个 OPTIONS 方法到服务器。)
            if (request.getMethod().equals("OPTIONS")) {
                System.out.println("跨域请求");
                return null;
            }
            //1.判断是否是登录
            if (request.getRequestURI().contains("/admin/login ")) {
                System.out.println("登录...");
                return null;
            }
            //2.判断是否是管理员角色
            String authorization = request.getHeader("Authorization");
            if (authorization!=null&& authorization.startsWith("Bearer ")) {
                String token= authorization.substring(7);
                Claims claims = jwtUtil.parseJWT(token);
                if (claims!=null) {
                    if ("admin".equals(claims.get("roles"))) {
                        System.out.println("通过了验证...");
                        context.addZuulRequestHeader("Authorization",authorization );
                        return null;
                    }
                }
            }
            Result result = new Result(false,StatusCode.ACCESSERROR,"权限不足");
            ObjectMapper mapper = new ObjectMapper();
            String resultStr = mapper.writeValueAsString(result);
            //3.其它终止执行
            context.setSendZuulResponse(false);//终止运行
            //http状态码
            context.setResponseStatusCode(402);
            HttpServletResponse response = context.getResponse();
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(resultStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
