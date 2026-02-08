package com.example.tunehub.dto;
import com.example.tunehub.model.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UsersProfileFullDTO {
    private String name;

    private String email;

    private String description;

    private Set<EUserType> userTypes = new HashSet<>();

    private Set<Role> roles = new HashSet<>();

    private LocalDate createdAt;

    private LocalDate editedIn;

    private boolean isActive;

    private String city;

    private String country;

    private Double rating;

    private List<UsersProfileDTO> followers;

    private List<UsersProfileDTO> following;

    private List<InstrumentResponseDTO> instrumentsUsers;

    private TeacherListingDTO teacher;

    private String imageProfilePath;

    private List<SheetMusicResponseDTO> sheetsMusic;

    private List<PostResponseDTO> posts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<EUserType> getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(Set<EUserType> userTypes) {
        this.userTypes = userTypes;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getEditedIn() {
        return editedIn;
    }

    public void setEditedIn(LocalDate editedIn) {
        this.editedIn = editedIn;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<UsersProfileDTO> getFollowers() {
        return followers;
    }

    public void setFollowers(List<UsersProfileDTO> followers) {
        this.followers = followers;
    }

    public List<UsersProfileDTO> getFollowing() {
        return following;
    }

    public void setFollowing(List<UsersProfileDTO> following) {
        this.following = following;
    }

    public List<InstrumentResponseDTO> getInstrumentsUsers() {
        return instrumentsUsers;
    }

    public void setInstrumentsUsers(List<InstrumentResponseDTO> instrumentsUsers) {
        this.instrumentsUsers = instrumentsUsers;
    }

    public TeacherListingDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherListingDTO teacher) {
        this.teacher = teacher;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getImageProfilePath() {
        return imageProfilePath;
    }

    public void setImageProfilePath(String imageProfilePath) {
        this.imageProfilePath = imageProfilePath;
    }

    public List<SheetMusicResponseDTO> getSheetsMusic() {
        return sheetsMusic;
    }

    public void setSheetsMusic(List<SheetMusicResponseDTO> sheetsMusic) {
        this.sheetsMusic = sheetsMusic;
    }

    public List<PostResponseDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponseDTO> posts) {
        this.posts = posts;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
