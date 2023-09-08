package com.capstone.mall.configuration;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class LogConfig {

    @Pointcut("execution(* com.capstone.mall.controller.*Controller.*(..)) && !@annotation(com.capstone.mall.configuration.NoLogging)")
    public void allController() {
    }

    ;

    @Pointcut("execution(* com.capstone.mall.service.*.*.*(..))")
    public void allService() {
    }

    ;

//    @Pointcut("execution(* com.capstone.mall.repository.*.*(..))")
//    public void allRepository() {
//    }

    ;

    @Around("allController() || allService()")
    public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("==============================execution method==============================");
        log.info("Target: {}", joinPoint.getTarget());
        log.info("Signature: {}", joinPoint.getSignature().getName());

        Object result = joinPoint.proceed();
        return result;
    }
}
