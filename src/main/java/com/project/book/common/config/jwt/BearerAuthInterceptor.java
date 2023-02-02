package com.project.book.common.config.jwt;

import com.project.book.common.exception.InvalidAccessTokenException;
import com.project.book.common.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class BearerAuthInterceptor implements HandlerInterceptor {

    private final AuthorizationExtractor authExtractor;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @Override
    public boolean preHandle (
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {

        String token = authExtractor.extract(request);
        if (redisUtil.getBlackListData(token) != null) {
            return false;
        }

        String oAuth = Optional.ofNullable(token)
                .filter(t -> jwtTokenProvider.validateToken(t))
                .map(t -> jwtTokenProvider.getPayload(t).getSubject())
                .orElseThrow(InvalidAccessTokenException::new);
        request.setAttribute("oAuth",oAuth);

        return true;
    }
}
