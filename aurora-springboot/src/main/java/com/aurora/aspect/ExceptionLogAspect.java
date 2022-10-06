package com.aurora.aspect;

import com.alibaba.fastjson.JSON;
import com.aurora.entity.ExceptionLog;
import com.aurora.event.ExceptionLogEvent;
import com.aurora.util.ExceptionUtil;
import com.aurora.util.IpUtil;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
public class ExceptionLogAspect {

    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut("execution(* com.aurora.controller..*.*(..))")
    public void exceptionLogPointcut() {
    }

    @AfterThrowing(value = "exceptionLogPointcut()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Exception e) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) Objects.requireNonNull(requestAttributes).resolveReference(RequestAttributes.REFERENCE_REQUEST);
        ExceptionLog exceptionLog = new ExceptionLog();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        exceptionLog.setOptUri(Objects.requireNonNull(request).getRequestURI());
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        methodName = className + "." + methodName;
        exceptionLog.setOptMethod(methodName);
        exceptionLog.setRequestMethod(Objects.requireNonNull(request).getMethod());
        if (joinPoint.getArgs().length > 0) {
            if (joinPoint.getArgs()[0] instanceof MultipartFile) {
                exceptionLog.setRequestParam("file");
            } else {
                exceptionLog.setRequestParam(JSON.toJSONString(joinPoint.getArgs()));
            }
        }
        if (Objects.nonNull(apiOperation)) {
            exceptionLog.setOptDesc(apiOperation.value());
        } else {
            exceptionLog.setOptDesc("");
        }
        exceptionLog.setExceptionInfo(ExceptionUtil.getTrace(e));
        String ipAddress = IpUtil.getIpAddress(request);
        exceptionLog.setIpAddress(ipAddress);
        exceptionLog.setIpSource(IpUtil.getIpSource(ipAddress));
        applicationContext.publishEvent(new ExceptionLogEvent(exceptionLog));
    }

}
