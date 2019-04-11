package com.tensquare.qa.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author:Haicz
 * @date:2019/03/02
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        if (authorization!=null&&authorization.startsWith("Bearer")){
            String token = authorization.substring(7);
            Claims claims = jwtUtil.parseJWT(token);
            if (claims!=null){
                if ("admin".equals(claims.get("roles"))) { //如果是管理员
                    request.setAttribute("admin_claims", claims);
                }
                if ("user".equals(claims.get("roles"))) { //如果是用户
                    request.setAttribute("user_claims", claims);
                }
            }
        }
        return true;
    }
}
