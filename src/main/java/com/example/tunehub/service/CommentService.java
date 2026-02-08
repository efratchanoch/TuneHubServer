package com.example.tunehub.service;

import com.example.tunehub.dto.CommentDTO;
import com.example.tunehub.dto.CommentPageDTO;
import com.example.tunehub.dto.CommentUploadDTO;
import com.example.tunehub.model.Comment;
import com.example.tunehub.model.Post;
import com.example.tunehub.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CommentService {

    private final UsersRepository usersRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final AuthService authService;

    @Autowired
    public CommentService(UsersRepository usersRepository,
                          CommentRepository commentRepository,
                          CommentMapper commentMapper,
                          PostRepository postRepository,
                          LikeRepository likeRepository,
                          AuthService authService) {
        this.usersRepository = usersRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.authService = authService;
    }


    public Comment getCommentById(Long id) {
        return commentRepository.findCommentById(id);
    }


    public CommentPageDTO getCommentsByPostPaged(Long postId, int page, int size) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentsPage =
                commentRepository.findByPostIdOrderByDateUploadedDesc(post.getId(), pageable);

        List<CommentDTO> dtos = commentMapper.commentToCommentDTO(
                commentsPage.getContent(),
                authService.getCurrentUserId(),
                likeRepository
        );

        CommentPageDTO dto = new CommentPageDTO();
        dto.setComments(dtos);
        dto.setTotalElements(commentsPage.getTotalElements());
        dto.setTotalPages(commentsPage.getTotalPages());
        dto.setCurrentPage(page);

        return dto;
    }


    public CommentDTO uploadComment(CommentUploadDTO dto) {

        Post post = postRepository.findById(dto.postId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        Users user = authService.getCurrentUser();

        Comment comment = commentMapper.commentUploadDTOToComment(dto);
        comment.setPost(post);
        comment.setUser(user);
        Comment saved = commentRepository.save(comment);

        return commentMapper.commentToCommentDTO(saved, user.getId(), likeRepository);
    }
}
