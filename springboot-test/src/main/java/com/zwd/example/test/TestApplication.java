package com.zwd.example.test;

import com.zwd.example.test.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.zwd.example.test"})
public class TestApplication {


    @Autowired
    private User user;

    public static void main(String[] args) {

        SpringApplication.run(TestApplication.class);
        new TestApplication().init();
    }

    private void init() {
        user.setName("朱卫东");
        user.setAge(18);
        System.out.println(user);
    }


}
