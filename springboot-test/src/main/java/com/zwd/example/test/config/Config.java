package com.zwd.example.test.config;

import com.zwd.example.test.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public User getUser() {
        System.out.println("zzz");
        return new User();
    }

}
