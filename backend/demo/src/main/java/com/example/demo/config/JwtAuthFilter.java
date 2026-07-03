package com.example.demo.config;

import com.example.demo.common.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 认证过滤器 —— 从请求头中提取 Token 并校验
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");

            // 调试日志：打印请求 URI 和 Authorization 头
            log.info("请求: {} {}, Authorization: {}",
                    request.getMethod(), request.getRequestURI(), authHeader);

            // 没有 Token 则直接放行（后续会被 Spring Security 拦截）
            if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            final String token = authHeader.substring(7);
            final String userPhon = jwtTokenUtil.extractUserPhon(token);

            if (userPhon != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtTokenUtil.validateToken(token, userPhon)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userPhon, null, Collections.emptyList());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            log.warn("JWT 认证失败: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
