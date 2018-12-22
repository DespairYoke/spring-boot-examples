package com.zwd.example.spring.outh.user;

import org.springframework.security.config.authentication.PasswordEncoderParser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwd
 * @date 2018/12/21 10:01
 * @Email stephen.zwd@gmail.com
 */
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_EMPLOYEE");
        grantedAuthorities.add(grantedAuthority);
        return new User("yunchongyin",new BCryptPasswordEncoder().encode("123456"),grantedAuthorities);
    }
}
