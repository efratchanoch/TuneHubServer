package com.example.tunehub.dto;

import com.example.tunehub.model.ETargetType;

import java.time.Instant;

public class FavoriteItemDTO {

    private Long id;
    private ETargetType targetType;
    private Long targetId;
    private Instant createdAt;
    private Object details;
    public FavoriteItemDTO() {}

    public FavoriteItemDTO(Long id, ETargetType targetType, Long targetId, Instant createdAt, Object details) {
        this.id = id;
        this.targetType = targetType;
        this.targetId = targetId;
        this.createdAt = createdAt;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }
}