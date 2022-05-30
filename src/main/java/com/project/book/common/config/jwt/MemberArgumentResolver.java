package com.project.book.common.config.jwt;

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


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        System.out.println("in MemberArgumentResolver");

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        System.out.println("request = " + request);
        String id = (String) request.getAttribute("id");
        System.out.println("id = " + id);

        Member member = Optional.ofNullable(id)
                .map(memberRepository::findByoAuth)
                .orElseThrow(MemberNotFoundException::new);

        return member;
    }
}
