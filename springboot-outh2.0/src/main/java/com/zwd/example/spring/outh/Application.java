package com.zwd.example.spring.outh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zwd
 * @date 2018/12/19 19:44
 * @Email stephen.zwd@gmail.com
 */

//@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        Runnable task = () -> {
          String threadName = Thread.currentThread().getName();
            System.out.println("Hello "+ threadName);
        };
        task.run();
//        SpringApplication.run(Application.class);
    }

}
