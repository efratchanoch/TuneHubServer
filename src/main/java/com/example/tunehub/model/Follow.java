package com.example.tunehub.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "follows", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"follower_id", "following_id"})
})
public class Follow {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "follower_id", nullable = false)
    private Long followerId;

    @Column(name = "following_id", nullable = false)
    private Long followingId;

    @Enumerated(EnumType.STRING)
    private EFollowStatus status;

    @CreationTimestamp
    private Instant createdAt;


    public Follow() {
    }


    public Follow(Long followerId, Long followingId, EFollowStatus status) {
        this.followerId = followerId;
        this.followingId = followingId;
        this.status = status;
    }


    public Follow(Long id, Long followerId, Long followingId, EFollowStatus status, Instant createdAt) {
        this.id = id;
        this.followerId = followerId;
        this.followingId = followingId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Long getFollowingId() {
        return followingId;
    }

    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }

    public EFollowStatus getStatus() {
        return status;
    }

    public void setStatus(EFollowStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}