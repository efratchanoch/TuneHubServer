package com.example.tunehub.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Users {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    private String password;

    private String email;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection(targetClass = EUserType.class)
    @CollectionTable(name = "user_types", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<EUserType> userTypes = new HashSet<>();

    private LocalDateTime createdAt;

    private LocalDateTime editedIn;

    @Transient
    private Double rating;

    private boolean isActive = false;

    private String city;

    private String country;

    @ManyToMany
    private List<Instrument> instrumentsUsers;

    @ManyToOne(fetch = FetchType.EAGER)
    private Teacher teacher;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Teacher teacherDetails;

    private String imageProfilePath;

    @OneToMany(mappedBy = "user")
    private List<SheetMusic> sheetsMusic;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> receivedNotifications;

    @OneToMany(mappedBy = "actor")
    private List<Notification> sentNotifications;

    @Column(nullable = false)
    private int followerCount = 0;

    // Security
    @ManyToMany
    private Set<Role> roles = new HashSet<>();
    public Users() {
    }

    public Users(Long id, String name, String password, String email, String description, Set<EUserType> userTypes, LocalDateTime createdAt, LocalDateTime editedIn, Double rating, boolean isActive, String city, String country, List<Instrument> instrumentsUsers, Teacher teacher, Teacher teacherDetails, String imageProfilePath, List<SheetMusic> sheetsMusic, List<Post> posts, List<Comment> comments, List<Notification> receivedNotifications, List<Notification> sentNotifications, int followerCount, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.description = description;
        this.userTypes = userTypes;
        this.createdAt = createdAt;
        this.editedIn = editedIn;
        this.rating = rating;
        this.isActive = isActive;
        this.city = city;
        this.country = country;
        this.instrumentsUsers = instrumentsUsers;
        this.teacher = teacher;
        this.teacherDetails = teacherDetails;
        this.imageProfilePath = imageProfilePath;
        this.sheetsMusic = sheetsMusic;
        this.posts = posts;
        this.comments = comments;
        this.receivedNotifications = receivedNotifications;
        this.sentNotifications = sentNotifications;
        this.followerCount = followerCount;
        this.roles = roles;
    }

    public List<Notification> getReceivedNotifications() {
        return receivedNotifications;
    }


    public void setReceivedNotifications(List<Notification> receivedNotifications) {
        this.receivedNotifications = receivedNotifications;
    }

    public List<Notification> getSentNotifications() {
        return sentNotifications;
    }

    public void setSentNotifications(List<Notification> sentNotifications) {
        this.sentNotifications = sentNotifications;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public Teacher getTeacherDetails() {
        return teacherDetails;
    }

    public void setTeacherDetails(Teacher teacherDetails) {
        this.teacherDetails = teacherDetails;
    }

    public String getCity() {
        return city;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
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

    public String getPassword() {
        return password;
    }

    public List<Instrument> getInstrumentsUsers() {
        return instrumentsUsers;
    }

    public void setInstrumentsUsers(List<Instrument> instrumentsUsers) {
        this.instrumentsUsers = instrumentsUsers;
    }

    public String getImageProfilePath() {
        return imageProfilePath;
    }

    public void setImageProfilePath(String imageProfilePath) {
        this.imageProfilePath = imageProfilePath;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getEditedIn() {
        return editedIn;
    }

    public void setEditedIn(LocalDateTime editedIn) {
        this.editedIn = editedIn;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<SheetMusic> getSheetsMusic() {
        return sheetsMusic;
    }

    public void setSheetsMusic(List<SheetMusic> sheetsMusic) {
        this.sheetsMusic = sheetsMusic;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }





}
