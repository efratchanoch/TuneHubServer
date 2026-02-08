package com.example.tunehub.dto;

import java.time.LocalDate;
import java.util.List;

public class PostDTO {
 private Long id;
 private String content;
 private LocalDate createdAt;
 private UsersProfileDTO profile;
 private List<CommentDTO> comments;
 private List<PostMediaDTO> media;

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getContent() {
  return content;
 }

 public void setContent(String content) {
  this.content = content;
 }

 public LocalDate getCreatedAt() {
  return createdAt;
 }

 public void setCreatedAt(LocalDate createdAt) {
  this.createdAt = createdAt;
 }

 public UsersProfileDTO getProfile() {
  return profile;
 }

 public void setProfile(UsersProfileDTO profile) {
  this.profile = profile;
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
}
