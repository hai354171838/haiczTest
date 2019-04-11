package com.tensquare;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author:Haicz
 * @date:2019/03/02
 */

public class CreateJwtTest {
    @Test
    public void test() {
        JwtBuilder builder = Jwts.builder().setId("999").setSubject("小红")
                .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+1000*360))
                .signWith(SignatureAlgorithm.HS256,"itcast" );
        System.out.println(builder.compact());

    }

    /**
     * 解析token
     */
    @Test
    public void test02() {
        String token="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiLlsI_nmb0iLCJpYXQiOjE1NTE0OTUxODN9.p92O0bYO3TTcRp4x7Y-emS670TRW7NTz2Uc_zVh3Jbg";
        Claims claims = Jwts.parser().setSigningKey("itcast").parseClaimsJws(token).getBody();
        System.out.println("Subject:"+claims.getSubject());
        System.out.println("Issuedat:"+claims.getIssuedAt());

    }
    /**
     * 解析token
     */
    @Test
    public void test03() {
        String token="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5OTkiLCJzdWIiOiLlsI_nuqIiLCJpYXQiOjE1NTE0OTYxOTIsImV4cCI6MTU1MTQ5NjU1Mn0.i3eFzswiHZfG9OWxaRJ0meUsF0Kux43n7QK4GvMz2Bc";
        Claims claims = Jwts.parser().setSigningKey("itcast").parseClaimsJws(token).getBody();
        System.out.println("id:"+claims.getId());
        System.out.println("subject:"+claims.getSubject());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy‐MM‐dd hh:mm:ss");
        System.out.println("签发时间:"+sdf.format(claims.getIssuedAt()));
        System.out.println("过期时间:"+sdf.format(claims.getExpiration()));
        System.out.println("当前时间:"+sdf.format(new Date()) );

    }
    @Test
    public void test04() {

        JwtBuilder builder = Jwts.builder().setId("999").setSubject("小黑")
                .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+1000*120))
                .claim("roles","admin" )
                .claim("logo","logo.png" )
                .signWith(SignatureAlgorithm.HS256,"itcast" );
        System.out.println(builder.compact());
    }

    @Test
    public void test05() {
        String token="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5OTkiLCJzdWIiOiLlsI_pu5EiLCJpYXQiOjE1NTE1MDc3NzMsImV4cCI6MTU1MTUwNzg5Mywicm9sZXMiOiJhZG1pbiIsImxvZ28iOiJsb2dvLnBuZyJ9.JlhF6T-zfjGJL5Q6Lkv-RcfuTQat-rtxUg1I_Y6Hs2Y";

        Claims claims = Jwts.parser().setSigningKey("itcast").parseClaimsJws(token).getBody();
        System.out.println("id:"+claims.getIssuedAt());
        System.out.println("subject:"+claims.getSubject());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy‐MM‐dd hh:mm:ss");
        System.out.println("签发时间:"+sdf.format(claims.getIssuedAt()));
        System.out.println("过期时间:"+sdf.format(claims.getExpiration()));
        System.out.println("当前时间:"+sdf.format(new Date()) );
        System.out.println("角色:"+claims.get("roles") );
        System.out.println("logo:"+claims.get("logo") );

    }
}
