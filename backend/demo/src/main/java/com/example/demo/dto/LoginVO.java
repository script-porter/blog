package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应体 —— 返回 JWT Token + 用户信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginVO {
    private String token;
    private String username;
}
