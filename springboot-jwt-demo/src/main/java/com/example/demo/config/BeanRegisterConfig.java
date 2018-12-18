package com.example.demo.config;

import com.example.demo.filter.MyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author zwd
 * @date 2018/12/18 16:34
 * @Email stephen.zwd@gmail.com
 */
@Configuration
public class BeanRegisterConfig {

    @Bean
    public FilterRegistrationBean createFilterBean() {
        //过滤器注册类
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new MyFilter());
        registration.addUrlPatterns("/user/hello"); //需要过滤的接口
        return registration;
    }
}
