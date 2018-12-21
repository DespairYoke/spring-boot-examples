package com.zwd.example.spring.outh.user;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

/**
 * @author zwd
 * @date 2018/12/21 09:38
 * @Email stephen.zwd@gmail.com
 */
public class MyAuthorizationServerTokenServices implements AuthorizationServerTokenServices {
    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication oAuth2Authentication) throws AuthenticationException {
        System.out.println("createAccessToken");
        return null;
    }

    @Override
    public OAuth2AccessToken refreshAccessToken(String s, TokenRequest tokenRequest) throws AuthenticationException {
        System.out.println("refreshAccessToken");
        return null;
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication oAuth2Authentication) {
        System.out.println("getAccessToken");
        return null;
    }
}
