package com.fgbg.demo.aspect;

import com.fgbg.demo.utils.RequestUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 限制每个ip对同一个接口的访问频率
 */
@Component
@Aspect
@Slf4j
@RestController
public class IpLimiterAspect {
    @Autowired
    private RequestUtils requestUtils;

    // 每秒生成1个令牌, 同个ip访问同个接口的QPS为1
    private final double PERMIT_PER_SECOND = 1;

    // 创建本地缓存
    private final Cache<String, RateLimiter> limiterCache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();

    @Around("execution(* com.fgbg.demo.controller..*.*(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 构造key
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String methodName = proceedingJoinPoint.getTarget().getClass().getName() + "." + methodSignature.getName();
        String key = requestUtils.getCurrentIp() + "->" + methodName;

        // 获取key对应的RateLimiter
        RateLimiter rateLimiter = limiterCache.get(key, () -> RateLimiter.create(PERMIT_PER_SECOND));

        if (! rateLimiter.tryAcquire()) {
            // 如果不能立刻获取令牌, 说明访问速度大于1 次/s, 触发限流
            log.warn("访问过快, 触发限流");
            throw new RuntimeException("访问过快, 触发限流");
        }
        log.info("接口放行...");
        return proceedingJoinPoint.proceed();
    }
}
