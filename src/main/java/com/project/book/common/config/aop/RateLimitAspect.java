package com.project.book.common.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class RateLimitAspect {
    private final RateLimiter rateLimiter;

    public RateLimitAspect(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void rateLimitPointcut() {}

    @Around("rateLimitPointcut()")
    public Object rateLimitAround(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String identifier = request.getRemoteAddr();
        String methodName = joinPoint.getSignature().getName();
        if (!rateLimiter.isOneMethodAllowed(identifier, methodName, 1, 5) || !rateLimiter.isAllMethodAllowed(identifier,5,20)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests");
        }
        return joinPoint.proceed();
    }
}
