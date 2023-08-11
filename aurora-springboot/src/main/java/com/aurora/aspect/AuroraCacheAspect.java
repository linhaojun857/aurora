package com.aurora.aspect;

import com.aurora.annotation.AuroraCache;
import com.aurora.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.aurora.constant.RedisConstant.*;

/**
 * @author zyl
 * @date 2023/8/11 16:48
 */
@Component
@SuppressWarnings("all")
@Aspect
@Slf4j
public class AuroraCacheAspect {

    @Autowired
    private RedisService redisService;
    @Autowired
    private RedissonClient redissonClient;

    @Around("@annotation(com.aurora.annotation.AuroraCache)")
    public Object auroraCacheAspectMethod(ProceedingJoinPoint point) throws Throwable {
        //  创建一个对象
        Object object = new Object();
        //  获取到注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        AuroraCache gmallCache = signature.getMethod().getAnnotation(AuroraCache.class);
        //  获取前缀
        String prefix = gmallCache.prefix();
        //  获取后缀
        String suffix = gmallCache.suffix();
        //  获取参数
        Object[] args = point.getArgs();
        //  定义缓存的key
        String key = prefix + Arrays.asList(args) + suffix;
        try {
            //  从缓存中获取数据进行判断.
            object = this.redisService.get(key);
            if (null == object) {
                //  缓存中没有数据
                //  定义一个locKey
                String locKey = prefix + Arrays.asList(args) + LOCK_SUFFIX;
                RLock lock = this.redissonClient.getLock(locKey);
                //  上锁
                boolean result = lock.tryLock(LOCK_EXPIRE_PX1, LOCK_EXPIRE_PX2, TimeUnit.SECONDS);
                //  判断
                if (result) {
                    //  表示获取到锁.
                    try {
                        //  查询数据库查询带有注解的方法体放入缓存.
                        object = point.proceed();
                        //  判断是否查询到数据
                        if (null == object) {
                            Object o = new Object();
                            this.redisService.set(key, o, KEY_TEMPORARY_TIMEOUT);
                            return o;
                        }
                        //  数据库中有数据
                        this.redisService.set(key, object, CODE_EXPIRE_TIME);
                        return object;
                    } finally {
                        //  解锁
                        lock.unlock();
                    }
                } else {
                    //  没有获取到锁.
                    TimeUnit.MILLISECONDS.sleep(100);
                    return auroraCacheAspectMethod(point);
                }
            } else {
                //  缓存有数据
                return object;
            }
        } catch (Throwable e) {
            log.error("查询数据异常,{}", args);
            e.printStackTrace();
        }
        //  直接查询数据库.
        return point.proceed();
    }
}
