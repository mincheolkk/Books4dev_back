package com.project.book.common.config.auth;

import com.project.book.member.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final LoginSuccessHandler loginSuccessHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors().and()
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .disable();

        //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http
                    .authorizeRequests()
                    .antMatchers("/member/login/**").authenticated()
                    .antMatchers("/book/login/**").authenticated()
                    .anyRequest().permitAll()
                .and()
                    .oauth2Login()
//                    .loginPage("/oauth2/authorization/kakao")
//                    .userInfoEndpoint()
//                    .userService(customOAuth2UserService)
//                .and()
                    .successHandler(loginSuccessHandler); // 로그인 성공 후 부가작업

    }
}