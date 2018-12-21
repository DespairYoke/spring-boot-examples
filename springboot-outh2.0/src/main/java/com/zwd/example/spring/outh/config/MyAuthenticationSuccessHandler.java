//package com.zwd.example.spring.outh.config;
//
//import org.apache.commons.codec.binary.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
//import org.springframework.security.oauth2.provider.*;
//import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
//import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//import static com.sun.xml.internal.ws.api.message.Packet.State.ServerResponse;
//
///**
// * @author zwd
// * @date 2018/12/20 16:42
// * @Email stephen.zwd@gmail.com
// */
////登录成功处理器
//@Component
//public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    private ClientDetailsService clientDetailsService;
//
//    @Autowired
//    private AuthorizationServerTokenServices authorizationServerTokenServices;
//
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        logger.info("登录成功");
//
//
//        String header = request.getHeader("Authorization");
//        if (header == null && !header.startsWith("Basic")) {
//            throw new UnapprovedClientAuthenticationException("请求投中无client信息");
//        }
//        String[] tokens = this.extractAndDecodeHeader(header, request);
//
//        assert tokens.length == 2;
//
//        //获取clientId 和 clientSecret
//        String clientId = tokens[0];
//        String clientSecret = tokens[1];
//
//        //获取 ClientDetails
//        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
//
//        if (clientDetails == null){
//            throw new UnapprovedClientAuthenticationException("clientId 不存在"+clientId);
//            //判断  方言  是否一致
//        }else if (!StringUtils.equals(clientDetails.getClientSecret(),clientSecret)){
//            throw new UnapprovedClientAuthenticationException("clientSecret 不匹配"+clientId);
//        }
//        //密码授权 模式, 组建 authentication
//        TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP,clientId,clientDetails.getScope(),"password");
//
//        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
//        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request,authentication);
//
//        OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
//
//        //判断是json 格式返回 还是 view 格式返回
//        //将 authention 信息打包成json格式返回
//        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().write(token.getValue());
//    }
//
//
//    /**
//     * 解码请求头
//     */
//    private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {
//        byte[] base64Token = header.substring(6).getBytes("UTF-8");
//
//        byte[] decoded;
//        try {
//            decoded = Base64.decode(base64Token);
//        } catch (IllegalArgumentException var7) {
//            throw new BadCredentialsException("Failed to decode basic authentication token");
//        }
//
//        String token = new String(decoded, "UTF-8");
//        int delim = token.indexOf(":");
//        if (delim == -1) {
//            throw new BadCredentialsException("Invalid basic authentication token");
//        } else {
//            return new String[]{token.substring(0, delim), token.substring(delim + 1)};
//        }
//    }
//}
//
//作者：微笑面对life
//        链接：https://www.jianshu.com/p/19059060036b
//        來源：简书
//        简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。