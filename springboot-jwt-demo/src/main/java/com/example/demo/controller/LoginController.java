package com.example.demo.controller;

import com.example.demo.util.JwtHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zwd
 * @date 2018/9/10 14:47
 * @Email stephen.zwd@gmail.com
 */
@RestController
public class LoginController {

    @RequestMapping("/user/login")
    public String login() {

        Map<String,Object> claims = new HashMap<>();
        claims.put("userid")

        Claims claims = Jwts.claims();
        claims.set

        String jwtToken = JwtHelper.createJWT("zzz",
                "1",
               "admin",
                "098f6bcd4621d373cade4e832627b4f6",
                "restapiuser",
                1000,
                "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=");

        String result_str = "bearer;" + jwtToken;
        return result_str;
    }

    @RequestMapping("user/hello")
    public String user(){
        return "helloworld";
    }
}
