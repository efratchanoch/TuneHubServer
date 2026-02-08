package com.example.tunehub.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class SheetMusicCategory {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "categories")
    private List<SheetMusic> sheetsMusic;

    public SheetMusicCategory() {
    }

    public SheetMusicCategory(long id, String name, List<SheetMusic> sheetsMusic) {
        this.id = id;
        this.name = name;
        this.sheetsMusic = sheetsMusic;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SheetMusic> getSheetsMusic() {
        return sheetsMusic;
    }

    public void setSheetsMusic(List<SheetMusic> sheetsMusic) {
        this.sheetsMusic = sheetsMusic;
    }
}
