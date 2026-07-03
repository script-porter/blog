package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 博客文章实体
 */
@Entity
@Table(name = "t_blog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 500)
    private String summary;

    @Column(length = 100)
    private String author;

    @Column(name = "cover_image", length = 500)
    private String coverImage;

    @Column(length = 255)
    private String tags;

    @Column(length = 50)
    private String category;

    @Column(nullable = false)
    private Integer status = 1;   // 1-草稿 2-已发布 3-下架

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "like_count")
    private Integer likeCount = 0;

    @Column(name = "comment_count")
    private Integer commentCount = 0;

    @Column(name = "is_top")
    private Boolean isTop = false;

    @Column(name = "is_original")
    private Boolean isOriginal = true;

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
