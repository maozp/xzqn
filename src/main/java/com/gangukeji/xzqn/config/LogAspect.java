package com.gangukeji.xzqn.config;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @Author: hx
 * @Date: 2019/5/30 20:40
 * @Description: 获取传入传出的参数
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.gangukeji.xzqn.config.Log)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        try {
            // 执行方法
            result = point.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        // 执行时长(毫秒)
        Long time = System.currentTimeMillis() - beginTime;
        // 处理日志
        dealRequest(point, time);
        if (result != null) {
            String string = result.toString();
            if (string.length() > 300) {
                string = string.substring(0, 300);
            }
            log.error("传出" + string);
            log.error("--------------------------------------------------------------");
        }
        return result;
    }

    private void dealRequest(ProceedingJoinPoint joinPoint, Long time) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
//        Log logAnnotation = method.getAnnotation(Log.class);
//        if (logAnnotation != null) {
//            // 注解上的描述
//            log.error("描述:" + logAnnotation.value());
//            log.error("耗时:" + time + "ms");
//        }
        // 请求的方法名
        // 请求的方法参数值
        Object[] args = joinPoint.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            String params = "";
            for (int i = 0; i < args.length; i++) {
                params += "  " + paramNames[i] + ": " + args[i];
            }
            log.error("--------------------------------------------------------------");
            log.error("uri:" + request.getRequestURI());
            log.error("传入:" + params);
        }
    }
}
