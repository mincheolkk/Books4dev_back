package com.project.book.common.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.book.common.config.jwt.JwtTokenProvider;
import com.project.book.common.config.jwt.RedisUtil;
import com.project.book.member.domain.Member;
import com.project.book.member.domain.Token;
import com.project.book.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

import static com.project.book.common.config.jwt.JwtTokenProvider.ACCESS_TOKEN_VALID_TIME;
import static com.project.book.common.config.jwt.JwtTokenProvider.REFRESH_TOKEN_VALID_TIME;

@RequiredArgsConstructor
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final RedisUtil redisUtil;

    private final MemberRepository memberRepository;


    private static String url= "http://localhost:8081/home";
    private static String typeUrl= "http://localhost:8081/selectPosition";

    @Override
    public void onAuthenticationSuccess(
                                        HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        System.out.println("authentication = " + authentication);
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String oAuth = String.valueOf(oauth2User.getAttributes().get("id"));
        String accessToken = jwtTokenProvider.createToken(oAuth, ACCESS_TOKEN_VALID_TIME).getValue();

        String randomValue = UUID.randomUUID().toString();
        Token refreshToken = jwtTokenProvider.createToken(randomValue, REFRESH_TOKEN_VALID_TIME);

        redisUtil.setRefreshToken(oAuth, refreshToken.getValue(), refreshToken.getExpiredTime());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("utf-8");
        response.addHeader("accessToken",accessToken);
        response.addHeader("refreshToken",refreshToken.getValue());
        System.out.println("accessToken = " + accessToken);


        redirectStrategy.sendRedirect(request,response,"http://localhost:8083/init?token="+accessToken);

    }

    protected void resultRedirectStrategy(
                                            HttpServletRequest request,
                                            HttpServletResponse response,
                                            Authentication authentication,
                                            String oAuth) throws IOException, ServletException {

        SavedRequest savedRequest = requestCache.getRequest(request, response);


        Member member = memberRepository.findByoAuth(oAuth);
        if (member.getType() == null) {
            redirectStrategy.sendRedirect(request, response, typeUrl);

        } else {
            if(savedRequest!=null) {
                String targetUrl = savedRequest.getRedirectUrl();
                redirectStrategy.sendRedirect(request, response, targetUrl);
            } else {
                redirectStrategy.sendRedirect(request, response, url);
            }
        }
    }


}
