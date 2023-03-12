package com.project.book.common.config.jwt;

import com.project.book.common.exception.MemberNotFoundException;
import com.project.book.member.domain.Member;
import com.project.book.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
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
    private final JwtTokenProvider jwtTokenProvider;

    private final AuthorizationExtractor authExtractor;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        String token = authExtractor.extract(request);
        Claims payload = jwtTokenProvider.getPayload(token);
        String oAuth = payload.getSubject();

        Member member = Optional.ofNullable(oAuth)
                .map(memberRepository::findByoAuth)
                .orElseThrow(MemberNotFoundException::new);

        return member;
    }
}
