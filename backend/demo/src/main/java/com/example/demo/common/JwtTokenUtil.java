package com.example.demo.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    // 1. 固定密钥（Base64 编码的 512 位密钥），重启后 Token 仍然有效
    private static final String SECRET_KEY_BASE64 = "dGhpcyBpcyBhIDUxMi1iaXQgS1NZIEtFWSBmb3IgSFM1MTIgYWxnb3JpdGhtIHVzZWQgaW4gdGhpcyBhcHBsaWNhdGlvbi4gSXQgbXVzdCBiZSBhdCBsZWFzdCA2NCBieXRlcyBsb25nLg==";
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10小时

    // 生成安全的签名密钥
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY_BASE64);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 2. 从用户信息生成Token[reference:6][reference:7]
    public String generateToken(String userPhon, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .setClaims(extraClaims) // 设置自定义声明（如用户ID、角色等）[reference:8]
                .setSubject(userPhon) // 设置主题，通常为用户名[reference:9]
                .setIssuedAt(new Date())// 设置签发时间[reference:10]
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 设置过期时间[reference:11]
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // 使用HS512算法签名[reference:12]
                .compact();
    }

    // 重载方法，简化调用
    public String generateToken(String userPhon) {
        return generateToken(userPhon, new HashMap<>());
    }

    // 3. 从Token中提取用户名
    public String extractUserPhon(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 4. 提取所有声明（Claims）
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 5. 提取特定的声明
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 6. 验证Token是否有效（检查用户名和是否过期）
    public Boolean validateToken(String token, String userPhon) {
        final String extractedUserPhon = extractUserPhon(token);
        return (extractedUserPhon.equals(userPhon) && !isTokenExpired(token));
    }

    // 7. 检查Token是否过期
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 8. 提取过期时间
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
