package com.example.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.time.Instant;

/**
 * 全局拦截器 —— 记录请求日志、统计执行耗时
 */
@Slf4j
@Component
public class GlobalInterceptor implements HandlerInterceptor {

    private static final String START_TIME_ATTR = "_startTime";

    /**
     * 请求处理前执行（Controller 方法调用之前）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 记录请求开始时间
        request.setAttribute(START_TIME_ATTR, Instant.now());

        log.info("▶️ [{}] {}?{}",
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString() != null ? request.getQueryString() : "");

        // 返回 true 表示继续执行，false 则中断请求
        return true;
    }

    /**
     * 请求处理后、视图渲染前执行
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) {
        // 可在此处对 ModelAndView 做统一处理
    }

    /**
     * 整个请求完成后执行（无论是否发生异常）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        Instant start = (Instant) request.getAttribute(START_TIME_ATTR);
        if (start != null) {
            long ms = Duration.between(start, Instant.now()).toMillis();
            log.info("◀️ [{}] {} 完成 | 状态={} | 耗时={}ms",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    ms);
        }

        if (ex != null) {
            log.error("❌ 请求异常: {}", ex.getMessage(), ex);
        }
    }
}
