package com.liumapp.demo.admin.eureka;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@SpringBootApplication
@EnableEurekaServer
public class AdminEurekaMain {

    public static void main (String[] args) {
        SpringApplication.run(AdminEurekaMain.class, args);
    }

}
