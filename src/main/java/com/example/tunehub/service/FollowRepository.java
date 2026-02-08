package com.example.tunehub.service;

import com.example.tunehub.model.EFollowStatus;
import com.example.tunehub.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Follow findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Optional<Follow> findByFollowerIdAndFollowingIdAndStatus(
            Long followerId, Long followingId, EFollowStatus status
    );

    boolean existsByFollowerIdAndFollowingIdAndStatus(
            Long followerId, Long followingId, EFollowStatus status
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT f FROM Follow f WHERE f.followerId = :followerId AND f.followingId = :followingId")
    Follow findByFollowerIdAndFollowingIdForUpdate(
            @Param("followerId") Long followerId,
            @Param("followingId") Long followingId
    );

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followingId = :userId AND f.status = :status")
    int countFollowersByUserIdAndStatus(
            @Param("userId") Long userId,
            @Param("status") EFollowStatus status
    );

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followerId = :userId AND f.status = :status")
    int countFollowingByUserIdAndStatus(
            @Param("userId") Long userId,
            @Param("status") EFollowStatus status
    );
}
