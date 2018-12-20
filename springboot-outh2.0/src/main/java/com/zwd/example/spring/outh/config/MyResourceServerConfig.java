package com.zwd.example.spring.outh.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author zwd
 * @date 2018/12/20 16:38
 * @Email stephen.zwd@gmail.com
 */
//资源服务器
@Configuration
@EnableResourceServer
public class MyResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private MyAuthenticationFailHandler myAuthenticationFailHandler;
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //表单登录 方式
        http.formLogin()
                .loginPage("/authentication/require")
                //登录需要经过的url请求
                .loginProcessingUrl("/authentication/form")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailHandler);

        http
                .authorizeRequests()
                .antMatchers("/user/*")
                .authenticated()
                .antMatchers("/oauth/token").permitAll()
                .anyRequest()
                .permitAll()
                .and()
                //关闭跨站请求防护
                .csrf().disable();
    }
}

作者：微笑面对life
        链接：https://www.jianshu.com/p/19059060036b
        來源：简书
        简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。