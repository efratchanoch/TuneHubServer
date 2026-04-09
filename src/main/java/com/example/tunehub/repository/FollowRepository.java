package com.example.tunehub.repository;

import com.example.tunehub.model.EFollowStatus;
import com.example.tunehub.model.Follow;
import com.example.tunehub.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.util.Optional;
import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Optional<Follow> findByFollowerIdAndFollowingIdAndStatus(
            Long followerId, Long followingId, EFollowStatus status
    );

    @Query("SELECT f.followerId FROM Follow f WHERE f.followingId = :followingId AND f.status = 'APPROVED'")
    List<Long> findAllFollowerIdsByFollowingId(@Param("followingId") Long followingId);

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

    // Retrieve all users who are following someone
    @Query("SELECT u FROM Users u JOIN Follow f ON u.id = f.followerId WHERE f.followingId = :userId AND f.status = 'APPROVED'")
    List<Users> findAllFollowersByUserId(@Param("userId") Long userId);

    // Retrieve all users who are being followed
    @Query("SELECT u FROM Users u JOIN Follow f ON u.id = f.followingId WHERE f.followerId = :userId AND f.status = 'APPROVED'")
    List<Users> findAllFollowingByUserId(@Param("userId") Long userId);
}
