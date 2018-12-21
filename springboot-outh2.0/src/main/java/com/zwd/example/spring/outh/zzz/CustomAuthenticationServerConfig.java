package com.zwd.example.spring.outh.zzz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * @author zwd
 * @date 2018/12/21 08:47
 * @Email stephen.zwd@gmail.com
 */
@Configuration
@EnableAuthorizationServer
public class CustomAuthenticationServerConfig extends AuthorizationServerConfigurerAdapter{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //用来配置令牌端点(Token Endpoint)的安全约束.
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    }

    //用来配置客户端详情服务
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("admin")
                .secret(passwordEncoder.encode("123456"))
                .accessTokenValiditySeconds(7200)
                .authorizedGrantTypes("refresh_token", "password","authorization_code")
                .redirectUris("https://github.com/despairyoke?tab=repositories")
                .scopes("/api/example/hello");
    }

    //来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }
}
