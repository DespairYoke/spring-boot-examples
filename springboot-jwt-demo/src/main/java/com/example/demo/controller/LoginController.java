package com.example.demo.controller;

import com.example.demo.util.JwtHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

        String jwtToken = JwtHelper.generateToken("123",456);

        return jwtToken;
    }

    @RequestMapping("user/hello")
    public String user(){

        return   "hello";
    }
}
