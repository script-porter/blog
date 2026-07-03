package com.example.demo.entity;

import java.time.LocalDateTime;

import com.example.demo.common.Sensitive;
import com.example.demo.common.SensitiveType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体
 */
@Entity
@Table(name = "t_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, comment = "昵称")
    private String username;

    @Sensitive(SensitiveType.PASSWORD)
    @Column(nullable = false, length = 100)
    private String password;

    @Sensitive(SensitiveType.PHONE)
    @Column(nullable = false, length = 20, updatable = false)
    private String phone;

    @Column(nullable = false)
    private Integer status = 1; // 1-正常 0-禁用 2-软删除

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
