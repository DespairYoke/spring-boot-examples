package com.zwd.example.spring.outh.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zwd
 * @date 2018/12/21 14:47
 * @Email stephen.zwd@gmail.com
 */
@RestController
@RequestMapping("/api/example")
public class UserController {


    @RequestMapping("hello")
    public String helloWorld() {

        return "hello world";
    }

    @RequestMapping("world")
    public String helloWorld1() {

        return "hello world";
    }


}
