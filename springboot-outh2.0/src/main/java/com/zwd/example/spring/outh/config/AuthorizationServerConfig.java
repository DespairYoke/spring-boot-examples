package com.zwd.example.spring.outh.config;

/**
 * @author zwd
 * @date 2018/12/19 19:45
 * @Email stephen.zwd@gmail.com
 */
public class AuthorizationServerConfig {
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.jdbc(dataSource)
//                .withClient("client")
//                .secret(passwordEncoder.encode("123456"))
//                .authorizedGrantTypes("password", "refresh_token")//允许授权范围 (密码授权和刷新令牌)
//                .authorities("ROLE_ADMIN", "ROLE_USER")//客户端可以使用的权限
//                .scopes("read", "write")
//                .accessTokenValiditySeconds(7200)
//                .refreshTokenValiditySeconds(10000)
//
//                //客户端模式
//                .and().withClient("client_1")
//                .secret(passwordEncoder.encode("123456"))
//                .authorizedGrantTypes("client_credentials")
//                .scopes("read", "write")
//                .authorities("client_credentials")
//                .accessTokenValiditySeconds(7200)
//
//                //授权码模式
//                .and()
                .withClient("client_code")
                .secret(passwordEncoder.encode("123456"))
                .authorizedGrantTypes("authorization_code", "refresh_token",
                        "password", "implicit")
                .scopes("all")
                .authorities("ROLE_ADMIN")
                .redirectUris("http://ww.baidu.com")
                .accessTokenValiditySeconds(1200)
                .refreshTokenValiditySeconds(50000);



    }

}
