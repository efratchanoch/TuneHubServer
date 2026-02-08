package com.example.tunehub.service;

import com.example.tunehub.model.ETargetType;
import com.example.tunehub.model.Like;
import org.hibernate.tool.schema.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserIdAndTargetTypeAndTargetId(Long userId, ETargetType targetType, Long targetId);

    Optional<Like> findByUserIdAndTargetTypeAndTargetId(Long currentUserId, ETargetType targetType, Long targetId);

    int countByTargetTypeAndTargetId(ETargetType targetType, Long targetId);

    void deleteAllByTargetTypeAndTargetId(ETargetType targetType, Long targetId);
}
