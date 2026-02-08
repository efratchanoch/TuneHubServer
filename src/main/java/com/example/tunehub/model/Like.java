package com.example.tunehub.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "target_type", "target_id"})
})
public class Like {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;


    @Column(name = "target_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ETargetType targetType;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @CreationTimestamp
    private Instant createdAt;

    public Like() {}
    public Like(Long userId, ETargetType targetType, Long targetId) {
        this.userId = userId;
        this.targetType = targetType;
        this.targetId = targetId;
    }


    public Like(Long id, Long userId, ETargetType targetType, Long targetId, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ETargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(ETargetType targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}