package com.example.tunehub.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Users user;

    private String title;

    private String content;

    private int hearts = 0;

    private int likes = 0;

    private Double rating;

    private String audioPath;

    private String videoPath;

    private LocalDateTime dateUploaded;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;

    private List<String> imagesPath;


    public Post() {
    }

    public Post(Long id, Users user, String title, String content, int hearts, int likes, Double rating, String audioPath, String videoPath, LocalDateTime dateUploaded, List<Comment> comments, List<String> imagesPath) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.hearts = hearts;
        this.likes = likes;
        this.rating = rating;
        this.audioPath = audioPath;
        this.videoPath = videoPath;
        this.dateUploaded = dateUploaded;
        this.comments = comments;
        this.imagesPath = imagesPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
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

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public LocalDateTime getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(LocalDateTime dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<String> getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(List<String> imagesPath) {
        this.imagesPath = imagesPath;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }


}

