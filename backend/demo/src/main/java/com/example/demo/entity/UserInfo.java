package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo.common.Sensitive;
import com.example.demo.common.SensitiveType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户扩展信息 —— 与 User 一对一，共享主键
 */
@Entity
@Table(name = "t_user_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    /** 主键即 User.id，一对一共享主键 */
    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    /** 个性签名 */
    @Column(length = 200)
    private String signature;

    @Sensitive(SensitiveType.EMAIL)
    @Column(length = 100)
    private String email;

    /** 头像 URL */
    @Column(length = 500)
    private String avatar;

    /** 职业 */
    @Column(length = 100)
    private String occupation;

    /** 所在地 */
    @Sensitive(SensitiveType.ADDRESS)
    @Column(length = 200)
    private String address;

    /** 生日 */
    @Column(length = 20)
    private LocalDate birthday;

    /** 证件号 */
    @Sensitive(SensitiveType.ID_CARD)
    @Column(length = 50)
    private String identify;

    /** 创建时间 */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** 更新时间 */
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
