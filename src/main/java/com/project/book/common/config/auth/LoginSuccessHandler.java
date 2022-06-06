package com.project.book.common.config.auth;

import com.project.book.common.config.jwt.JwtTokenProvider;
import com.project.book.common.config.jwt.RedisUtil;
import com.project.book.member.domain.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final RedisUtil redisUtil;

    private static String url= "http://localhost:8080";

    @Override
    public void onAuthenticationSuccess(
                                        HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        System.out.println("in LoginSuccessHandler , onAuth~");

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String oAuth = String.valueOf(oauth2User.getAttributes().get("id"));
        String token = jwtTokenProvider.createAccessToken(oAuth).getValue();

        System.out.println("oauth2User = " + oauth2User);
        System.out.println("oAuth = " + oAuth);
        System.out.println("token = " + token);


        String randomValue = UUID.randomUUID().toString();
        Token refreshToken = jwtTokenProvider.createRefreshToken(randomValue);

        redisUtil.setRefreshToken(oAuth, refreshToken.getValue(), refreshToken.getExpiredTime());

        System.out.println("end onAuthenticationSuccess");

//
//        resultRedirectStrategy(request, response, authentication);
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
    }

    protected void resultRedirectStrategy(
                                            HttpServletRequest request,
                                            HttpServletResponse response,
                                            Authentication authentication) throws IOException, ServletException {

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        System.out.println("savedRequest = " + savedRequest);
        System.out.println("in resultRedirectStrategy");

        if(savedRequest!=null) {
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            redirectStrategy.sendRedirect(request, response, url);
        }

    }


}
