package com.project.book.common.config.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DistributedLock {
    String key();
    long waitTime() default 10L;
    long leaseTime() default 5L;
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}