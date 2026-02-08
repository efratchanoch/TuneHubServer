package com.example.tunehub.dto;

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

    private List<CommentDTO> comments; // רשימת תגובות מלאה
    private List<PostMediaDTO> media; // המדיה המצורפת

    private boolean isFavorite; // האם המשתמש הנוכחי סימן את הפוסט כמועדף (נתון מותאם אישית)


    // קונסטרוקטורים
    public PostDetailsDTO() {
    }

    public PostDetailsDTO(Long id, String title, String content, LocalDate dateUploaded, int hearts, int likes, UsersProfileDTO user, List<CommentDTO> comments, List<PostMediaDTO> media, boolean isFavorite) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateUploaded = dateUploaded;
        this.hearts = hearts;
        this.likes = likes;
        this.user = user;
        this.comments = comments;
        this.media = media;
        this.isFavorite = isFavorite;
    }

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

