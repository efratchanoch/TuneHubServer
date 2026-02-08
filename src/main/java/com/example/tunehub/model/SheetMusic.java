package com.example.tunehub.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class SheetMusic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private EScale scale;

    private int likes = 0;

    private String fileName;

    private int hearts = 0;

    private int downloads = 0;

    @Transient
    private Double rating;

    private LocalDateTime dateUploaded;

    @Enumerated(EnumType.STRING)
    private EDifficultyLevel level;

    private int pages;

    @ManyToOne
    private Users user;

    @ManyToMany
    private List<Instrument> instruments;

    @ManyToMany
    private List<SheetMusicCategory> categories;

    private String composer;

    private String lyricist;

    private String imageCoverName;

    public SheetMusic() {
    }

    public SheetMusic(Long id, String title, EScale scale, int likes, String fileName, int hearts, int downloads, Double rating, LocalDateTime dateUploaded, EDifficultyLevel level, int pages, Users user, List<Instrument> instruments, List<SheetMusicCategory> categories, String composer, String lyricist, String imageCoverName) {
        this.id = id;
        this.title = title;
        this.scale = scale;
        this.likes = likes;
        this.fileName = fileName;
        this.hearts = hearts;
        this.downloads = downloads;
        this.rating = rating;
        this.dateUploaded = dateUploaded;
        this.level = level;
        this.pages = pages;
        this.user = user;
        this.instruments = instruments;
        this.categories = categories;
        this.composer = composer;
        this.lyricist = lyricist;
        this.imageCoverName = imageCoverName;
    }

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

    public EScale getScale() {
        return scale;
    }

    public void setScale(EScale scale) {
        this.scale = scale;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public LocalDateTime getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(LocalDateTime dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public EDifficultyLevel getLevel() {
        return level;
    }

    public void setLevel(EDifficultyLevel level) {
        this.level = level;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<Instrument> instruments) {
        this.instruments = instruments;
    }

    public List<SheetMusicCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<SheetMusicCategory> categories) {
        this.categories = categories;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getLyricist() {
        return lyricist;
    }

    public void setLyricist(String lyricist) {
        this.lyricist = lyricist;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getImageCoverName() {
        return imageCoverName;
    }

    public void setImageCoverName(String imageCoverName) {
        this.imageCoverName = imageCoverName;
    }
}


