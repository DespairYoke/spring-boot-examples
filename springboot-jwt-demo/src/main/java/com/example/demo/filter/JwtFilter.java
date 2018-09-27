package com.example.demo.filter;

import com.example.demo.util.JwtHelper;
import io.jsonwebtoken.Claims;
import org.springframework.web.filter.GenericFilterBean;

import javax.security.auth.login.LoginException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zwd
 * @date 2018/9/10 11:14
 * @Email stephen.zwd@gmail.com
 */
public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse)res;
        String authHeader = request.getHeader("authorization");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(req, res);
        } else {

            if (authHeader == null || !authHeader.startsWith("bearer;")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            final String token = authHeader.substring(7);

            try {
                final Claims claims = JwtHelper.parseJWT(token,"MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=");
                if(claims == null){
                    throw new LoginException("zzz");
                }
                request.setAttribute("zwd", claims);
            } catch (final Exception e) {
                try {
                    throw new LoginException("zzzz");
                } catch (LoginException e1) {
                    e1.printStackTrace();
                }
            }

            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {

    }
}
