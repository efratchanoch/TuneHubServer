package com.example.tunehub.service;

import com.example.tunehub.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long post_id);

    Comment findCommentById(Long id);

    Comment save(Comment comment);

    Page<Comment> findByPostIdOrderByDateUploadedDesc(@Param("postId") Long postId, Pageable pageable);

    @Modifying
    @Query("UPDATE Comment c SET c.likes = :count WHERE c.id = :id")
    @Transactional
    void updateLikeCount(@Param("id") Long  commentId, @Param("count") int count);

    @Query("SELECT COALESCE(SUM(c.likes), 0) FROM Comment c WHERE c.user.id = :userId")
    long sumCommentLikes(@Param("userId") Long userId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.user.id = :userId")
    long countCommentsOnUserPosts(@Param("userId") Long userId);

    long countByUserId(Long userId);
}
