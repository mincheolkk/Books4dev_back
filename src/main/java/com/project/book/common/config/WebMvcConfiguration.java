package com.project.book.common.config;

import com.project.book.common.config.jwt.BearerAuthInterceptor;
import com.project.book.common.config.jwt.MemberArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MemberArgumentResolver MemberArgumentResolver;
    private final BearerAuthInterceptor bearerAuthInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(MemberArgumentResolver);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        final List<String> pathPatterns = List.of(
                "/auth/out","/auth/refreshtoken"
        );
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns(pathPatterns);
    }
}
