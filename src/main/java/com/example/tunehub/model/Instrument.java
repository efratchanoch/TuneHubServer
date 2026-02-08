package com.example.tunehub.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Instrument {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "instrumentsUsers")
    private List<Users> users;

    @ManyToMany(mappedBy = "instruments")
    private List<Teacher> teachers;

    @ManyToMany(mappedBy = "instruments")
    private List<SheetMusic> sheetsMusic;

    public Instrument() {
    }

    public Instrument(long id, String name, List<Users> users, List<Teacher> teachers, List<SheetMusic> sheetsMusic) {
        this.id = id;
        this.name = name;
        this.users = users;
        this.teachers = teachers;
        this.sheetsMusic = sheetsMusic;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<SheetMusic> getSheetsMusic() {
        return sheetsMusic;
    }

    public void setSheetsMusic(List<SheetMusic> sheetsMusic) {
        this.sheetsMusic = sheetsMusic;
    }
}
