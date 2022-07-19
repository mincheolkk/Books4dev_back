package com.project.book.common.config.auth;

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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    private static String typeUrl= "http://localhost:8081/search";

    @Override
    public void onAuthenticationSuccess(
                                        HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        System.out.println("in LoginSuccessHandler , onAuth~");

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String oAuth = String.valueOf(oauth2User.getAttributes().get("id"));
        System.out.println("oAuth = " + oAuth);
        String token = jwtTokenProvider.createToken(oAuth, ACCESS_TOKEN_VALID_TIME).getValue();

        System.out.println("oauth2User = " + oauth2User);
        System.out.println("oAuth = " + oAuth);
        System.out.println("token = " + token);


        String randomValue = UUID.randomUUID().toString();
        Token refreshToken = jwtTokenProvider.createToken(randomValue, REFRESH_TOKEN_VALID_TIME);

        redisUtil.setRefreshToken(oAuth, refreshToken.getValue(), refreshToken.getExpiredTime());

        System.out.println("end onAuthenticationSuccess");

//
//

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("utf-8");
        response.addHeader("accessToken",token);
        response.addHeader("refreshToken",refreshToken.getValue());
//        response.addCookie("refreshToken",refreshToken);

        System.out.println("response.getHeader(\"refreshToken\") = " + response.getHeader("refreshToken"));
//        response.getWriter()
//                .write(objectMapper.writeValueAsString(
//                        AuthTokenResponse.of(member.getId(), member.getNickname(),
//                                member.getAvatar(), token, AuthorizationType.BEARER)));
        resultRedirectStrategy(request, response, authentication, oAuth);

    }

    protected void resultRedirectStrategy(
                                            HttpServletRequest request,
                                            HttpServletResponse response,
                                            Authentication authentication,
                                            String oAuth) throws IOException, ServletException {

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        System.out.println("request = " + request);
        System.out.println("response = " + response);
        System.out.println("savedRequest = " + savedRequest);
        System.out.println("in resultRedirectStrategy");
        System.out.println("authentication = " + authentication);


        Member member = memberRepository.findByoAuth(oAuth);
        System.out.println("member = " + member);
        if (member.getType() == null) {
            System.out.println("member.getType() = " + member.getType());
            System.out.println("2222");
            redirectStrategy.sendRedirect(request, response, typeUrl);
        } else {
            if(savedRequest!=null) {
                String targetUrl = savedRequest.getRedirectUrl();
                System.out.println("targetUrl = " + targetUrl);
                redirectStrategy.sendRedirect(request, response, targetUrl);
            } else {
                redirectStrategy.sendRedirect(request, response, url);
            }
        }

        // 가입 시키고 여기서 분기처리하자. 개발자 포지션 체크 안 되있으면 개발자 포지션 고르는 화면으로 이동시키기.
//        if(savedRequest!=null) {
//            String targetUrl = savedRequest.getRedirectUrl();
//            System.out.println("targetUrl = " + targetUrl);
//            redirectStrategy.sendRedirect(request, response, targetUrl);
//        } else {
//            redirectStrategy.sendRedirect(request, response, url);
//        }

    }


}
