package com.example.tunehub.dto.post;

import com.example.tunehub.dto.user.UsersProfileDTO;
import com.example.tunehub.dto.comment.CommentDTO;

import java.time.LocalDate;
import java.util.List;


public class PostDetailsDTO {

    private Long id;
    private String title;
    private String content;
    private LocalDate dateUploaded;

    private int hearts;
    private int likes;

    private UsersProfileDTO user;

    private List<CommentDTO> comments;
    private List<PostMediaDTO> media;

    private boolean isFavorite;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(LocalDate dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public UsersProfileDTO getUser() {
        return user;
    }

    public void setUser(UsersProfileDTO user) {
        this.user = user;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public List<PostMediaDTO> getMedia() {
        return media;
    }

    public void setMedia(List<PostMediaDTO> media) {
        this.media = media;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}

