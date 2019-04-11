package com.tensquare.user;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author:Haicz
 * @date:2019/03/02
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //authorizeRequests: 请求授权
        //antMatchers("/**").permitAll(): 任何用户都可以访问任何资源
        //anyRequest().authenticated(): 没有匹配上面的路径,只有用户被认证
        //and().csrf().disable(): 关闭跨域安全请求
        http.authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }
}
