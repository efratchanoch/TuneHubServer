package com.example.tunehub.controller;

import com.example.tunehub.dto.CommentDTO;
import com.example.tunehub.dto.CommentPageDTO;
import com.example.tunehub.dto.CommentUploadDTO;
import com.example.tunehub.model.Comment;
import com.example.tunehub.service.CommentService;
import com.example.tunehub.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    private final AuthService authService;

    @Autowired
    public CommentController(CommentService commentService, AuthService authService) {
        this.commentService = commentService;
        this.authService = authService;
    }

    @GetMapping("/commentById/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        try {
            Long userId = authService.getCurrentUserId();
            Comment comment = commentService.getCommentById(id);
            return ResponseEntity.ok(comment);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/byPost/{postId}/paged")
    public ResponseEntity<CommentPageDTO> getCommentsByPostPaged(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Long userId = authService.getCurrentUserId();
            CommentPageDTO result = commentService.getCommentsByPostPaged(postId, page, size);
            return ResponseEntity.ok(result);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<CommentDTO> uploadComment(@Valid @RequestBody CommentUploadDTO dto) {
        try {
            Long userId = authService.getCurrentUserId();
            CommentDTO created = commentService.uploadComment(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
