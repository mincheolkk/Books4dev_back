package com.project.book.common.config.jwt;

import com.project.book.common.exception.InvalidAccessTokenException;
import com.project.book.common.exception.MemberNotFoundException;
import com.project.book.member.domain.Member;
import com.project.book.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

    private final RedisUtil redisUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        System.out.println("in MemberArgumentResolver on resolveArgument");

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        System.out.println("request = " + request);
        String oAuth = (String) request.getAttribute("oAuth");
        System.out.println("oAuth = " + oAuth);

        Member member = Optional.ofNullable(oAuth)
                .map(memberRepository::findByoAuth)
                .orElseThrow(MemberNotFoundException::new);

        System.out.println("end on resolveArgument");
        return member;
    }
}
