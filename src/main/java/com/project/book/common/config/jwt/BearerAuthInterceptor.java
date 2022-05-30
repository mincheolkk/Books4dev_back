package com.project.book.common.config.jwt;

import com.project.book.common.exception.InvalidTokenException;
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

    @Override
    public boolean preHandle (
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        System.out.println("in preHandle");
        String token = authExtractor.extract(request);

        System.out.println("token = " + token);
        String id = Optional.ofNullable(token)
                .filter(t -> jwtTokenProvider.validateToken(t))
                .map(t -> jwtTokenProvider.getPayload(t))
                .orElseThrow(InvalidTokenException::new);

        request.setAttribute("id",id);

        return true;
    }
}
