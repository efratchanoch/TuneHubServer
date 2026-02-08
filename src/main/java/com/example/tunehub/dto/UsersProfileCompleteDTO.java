package com.example.tunehub.dto;

import com.example.tunehub.model.EUserType;
import com.example.tunehub.model.Instrument;
import com.example.tunehub.model.Post;
import com.example.tunehub.model.SheetMusic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class UsersProfileCompleteDTO {

    private Long id;
    private String name;
    private String email;
    private boolean active;
    private String description;
    private Double rating;
    private String city;
    private String country;
    private String imageProfilePath;

    private Set<EUserType> userTypes;
    private List<InstrumentResponseDTO> instruments;


    private List<SheetMusicResponseDTO> sheetsMusic;
    private List<PostResponseDTO> posts;

    private String createdAt;

    private String editedIn;

    private boolean isOwnProfile;
    private boolean canBeMyStudent;
    private boolean isMyStudent;
    private boolean canEditRoles;
    private boolean canDelete;

    private UsersProfileDTO teacher;
    private TeacherListingDTO teacherDetails;

    private long totalLikes;
    private long totalHearts;
    private long totalCommentsWritten;
    private long totalCommentsReceived;

    public long getTotalCommentsWritten() { return totalCommentsWritten; }
    public void setTotalCommentsWritten(long totalCommentsWritten) { this.totalCommentsWritten = totalCommentsWritten; }

    public long getTotalCommentsReceived() { return totalCommentsReceived; }
    public void setTotalCommentsReceived(long totalCommentsReceived) { this.totalCommentsReceived = totalCommentsReceived; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
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

    public String getImageProfilePath() {
        return imageProfilePath;
    }

    public void setImageProfilePath(String imageProfilePath) {
        this.imageProfilePath = imageProfilePath;
    }

    public Set<EUserType> getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(Set<EUserType> userTypes) {
        this.userTypes = userTypes;
    }

    public List<InstrumentResponseDTO> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<InstrumentResponseDTO> instruments) {
        this.instruments = instruments;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEditedIn() {
        return editedIn;
    }

    public void setEditedIn(String editedIn) {
        this.editedIn = editedIn;
    }

    public boolean isOwnProfile() {
        return isOwnProfile;
    }

    public void setOwnProfile(boolean ownProfile) {
        isOwnProfile = ownProfile;
    }

    public boolean isCanBeMyStudent() {
        return canBeMyStudent;
    }

    public void setCanBeMyStudent(boolean canBeMyStudent) {
        this.canBeMyStudent = canBeMyStudent;
    }

    public boolean isMyStudent() {
        return isMyStudent;
    }

    public void setMyStudent(boolean myStudent) {
        isMyStudent = myStudent;
    }

    public boolean isCanEditRoles() {
        return canEditRoles;
    }

    public void setCanEditRoles(boolean canEditRoles) {
        this.canEditRoles = canEditRoles;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public UsersProfileDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(UsersProfileDTO teacher) {
        this.teacher = teacher;
    }

    public TeacherListingDTO getTeacherDetails() {
        return teacherDetails;
    }

    public void setTeacherDetails(TeacherListingDTO teacherDetails) {
        this.teacherDetails = teacherDetails;
    }

    public long getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(long totalLikes) {
        this.totalLikes = totalLikes;
    }

    public long getTotalHearts() {
        return totalHearts;
    }

    public void setTotalHearts(long totalHearts) {
        this.totalHearts = totalHearts;
    }
}
