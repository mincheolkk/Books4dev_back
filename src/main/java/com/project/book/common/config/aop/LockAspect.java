package com.project.book.common.config.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LockAspect {

    private final RedissonClient redissonClient;
    private final Transaction4Aop transaction4Aop;

    @Around("@annotation(com.project.book.common.config.aop.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock annotation = method.getAnnotation(DistributedLock.class);

        String key = annotation.key();
        RLock lock = redissonClient.getLock(key);

        try {
            boolean available = lock.tryLock(annotation.waitTime(), annotation.leaseTime(), annotation.timeUnit());

            if (!available) {
                return false;
            }

            return transaction4Aop.proceed(joinPoint);
        } catch (InterruptedException | IOException e) {
            log.error("DistributedLock error : " + e.getMessage());
            throw new RuntimeException();
        } finally {
            if (lock.isHeldByCurrentThread() && lock.isLocked()) {
                lock.unlock();
            }
        }
    }
}
