package com.example.tunehub.dto;

import com.example.tunehub.model.Teacher;

import java.util.ArrayList;
import java.util.List;

public class GlobalSearchResponseDTO {
    private List<PostSearchDTO> posts = new ArrayList<>();
    private List<SheetMusicSearchDTO> sheetMusic = new ArrayList<>();
    private List<UsersSearchDTO> musicians = new ArrayList<>();
    private List<UsersSearchDTO> teachers = new ArrayList<>();


    public List<PostSearchDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostSearchDTO> posts) {
        this.posts = posts;
    }

    public List<SheetMusicSearchDTO> getSheetMusic() {
        return sheetMusic;
    }

    public void setSheetMusic(List<SheetMusicSearchDTO> sheetMusic) {
        this.sheetMusic = sheetMusic;
    }

    public List<UsersSearchDTO> getMusicians() {
        return musicians;
    }

    public void setMusicians(List<UsersSearchDTO> musicians) {
        this.musicians = musicians;
    }

    public List<UsersSearchDTO> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<UsersSearchDTO> teachers) {
        this.teachers = teachers;
    }

    public boolean hasResults() {
        return !posts.isEmpty() || !sheetMusic.isEmpty() || !musicians.isEmpty() || !teachers.isEmpty();
    }
}
