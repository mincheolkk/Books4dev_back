package com.project.book.common.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberArgumentResolver MemberArgumentResolver;
    private final BearerAuthInterceptor bearerAuthInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(MemberArgumentResolver);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        final List<String> pathPatterns = Arrays.asList(
               "/todo", "/member/login/**", "/book/login/**");
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns(pathPatterns);

        System.out.println("in WebMvcConfig on addInterceptors");
    }
}
