package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.interceptor.GlobalInterceptor;

/**
 * Web MVC 配置 —— 注册全局拦截器 & H2 Console Servlet
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private GlobalInterceptor globalInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册全局拦截器，拦截所有请求路径
        registry.addInterceptor(globalInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns( // 排除静态资源路径
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/webjars/**",
                        "/favicon.ico",
                        "/error",
                        "/h2-console",
                        "/h2-console/**",
                        "/h2-console/*");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 参数 strength = 10 是默认值，代表加密强度（迭代轮次）
        return new BCryptPasswordEncoder();
    }

}
