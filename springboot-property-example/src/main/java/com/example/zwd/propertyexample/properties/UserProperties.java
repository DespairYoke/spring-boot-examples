package com.example.zwd.propertyexample.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author zwd
 * @date 2018/9/29 15:11
 * @Email stephen.zwd@gmail.com
 */
@Component
public class UserProperties {

    @Value("${zwd.name}")
    private String username;
    @Value("${zwd.password}")
    private String password;

    @Value("${zwd.content}")
    private String content;
    @Value("${zwd.blog.value}")
    private String isString;
    @Value("${zwd.blog.number}")
    private Integer number;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsString() {
        return isString;
    }

    public void setIsString(String isString) {
        this.isString = isString;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
