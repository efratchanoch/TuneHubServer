package com.example.tunehub.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.OffsetDateTime;


@Entity
public class Notification {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Users user;

    @ManyToOne
    private Users actor;
    @Enumerated(EnumType.STRING)
    private ENotificationType type;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    private ETargetType targetType;

    @Enumerated(EnumType.STRING)
    private ENotificationCategory category;

    private Long targetId;

    private boolean isRead = false;

    private Integer count = 1;

    @CreationTimestamp
    private OffsetDateTime createdAt;

    public Notification() {
    }

    public Notification(ENotificationType type, Users user, Users actor, ETargetType targetType, Long targetId) {
        this.type = type;
        this.user = user;
        this.actor = actor;
        this.targetType = targetType;
        this.targetId = targetId;
        this.isRead = false;

        this.category = getCategoryByType(type);
        setTitleAndMessageBasedOnType(type, actor ,count);
    }

    public Notification(Long id, Users user, Users actor, ENotificationType type, String title, String message, ETargetType targetType, ENotificationCategory category, Long targetId, boolean isRead, Integer count, OffsetDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.actor = actor;
        this.type = type;
        this.title = title;
        this.message = message;
        this.targetType = targetType;
        this.category = category;
        this.targetId = targetId;
        this.isRead = isRead;
        this.count = 1;
        this.createdAt = createdAt;
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

    public Users getActor() {
        return actor;
    }

    public void setActor(Users actor) {
        this.actor = actor;
    }

    public ENotificationType getType() {
        return type;
    }

    public void setType(ENotificationType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ETargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(ETargetType targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ENotificationCategory getCategory() {
        return category;
    }

    public void setCategory(ENotificationCategory category) {
        this.category = category;
    }


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setTitleAndMessageBasedOnType(ENotificationType type, Users actor ,Integer count) {

        String actorName = (actor != null) ? actor.getName() : "Someone";
        String countString = (count != null && count > 1) ? count.toString() + " " : "";

        switch (type) {


            case LIKE_POST -> {
                this.title = "New Engagement";
                if (count != null && count > 1) {
                    this.message = count + " people liked your post.";
                } else {
                    this.message = actorName + " liked your post.";
                }
            }
            case LIKE_COMMENT -> {
                this.title = "New Engagement";
                if (count != null && count > 1) {
                    this.message = count + " people liked your comment.";
                } else {
                    this.message = actorName + " liked your comment.";
                }
            }
            case LIKE_MUSIC -> {
                this.title = "New Engagement";
                if (count != null && count > 1) {
                    this.message = count + " people liked your music track.";
                } else {
                    this.message = actorName + " liked your music track.";
                }
            }
            case FAVORITE_POST -> {
                this.title = "New Engagement";
                if (count != null && count > 1) {
                    this.message = count + " people added your post to favorites.";
                } else {
                    this.message = actorName + " added your post to favorites.";
                }
            }
            case FAVORITE_MUSIC -> {
                this.title = "New Engagement";
                if (count != null && count > 1) {
                    this.message = count + " people added your music track to favorites.";
                } else {
                    this.message = actorName + " added your music track to favorites.";
                }
            }


            case COMMENT_ON_POST -> {
                this.title = "New Comment on Your Post";
                this.message = actorName + " commented on your post.";
            }


            case FOLLOWEE_NEW_POST -> {
                this.title = "New Activity from Someone You Follow";
                this.message = actorName + " posted new content.";
            }
            case FOLLOWEE_NEW_MUSIC -> {
                this.title = "New Activity from Someone You Follow";
                this.message = actorName + " uploaded new music.";
            }
            case FOLLOWEE_NEW_PROFILE_PICTURE -> {
                this.title = "New Activity from Someone You Follow";
                this.message = actorName + " updated their profile picture.";
            }
            case FOLLOWEE_NEW_VIDEO -> {
                this.title = "New Activity from Someone You Follow";
                this.message = actorName + " uploaded a new video.";
            }
            case FOLLOWEE_PROFILE_UPDATED -> {
                this.title = "New Activity from Someone You Follow";
                this.message = actorName + " updated their profile.";
            }


            case FOLLOW_REQUEST_RECEIVED -> {
                this.title = "New Follow Request";
                this.message = actorName + " wants to follow you.";
            }
            case FOLLOWER_REMOVED -> {
                this.title = "Follower Update";
                this.message = actorName + " removed you from their followers.";
            }


            case FOLLOW_REQUEST_ACCEPTED -> {
                this.title = "Follow Request Accepted";
                this.message = actorName + " approved your follow request.";
            }
            case FOLLOW_REQUEST_DECLINED -> {
                this.title = "Follow Request Declined";
                this.message = actorName + " declined your follow request.";
            }


            case ADMIN_WARNING_POST -> {
                this.title = "Content Warning";
                this.message = "Your post violates our content guidelines. Continued issues may result in account removal.";
            }
            case ADMIN_WARNING_COMMENT -> {
                this.title = "Content Warning";
                this.message = "Your comment violates our content guidelines. Continued issues may result in account removal.";
            }
            case ADMIN_DELETED_POST -> {
                this.title = "Post Removed";
                this.message = "Your post was removed due to a policy violation.";
            }
            case ADMIN_DELETED_COMMENT -> {
                this.title = "Comment Removed";
                this.message = "Your comment was removed due to a policy violation.";
            }
            case ADMIN_PROMOTION_MANAGER -> {
                this.title = "You’ve Been Granted Moderator Privileges";
                this.message = "You can now moderate user content.";
            }
            case ADMIN_PROMOTION_SUPER_MANAGER -> {
                this.title = "You’ve Been Granted Super-Moderator Privileges";
                this.message = "You can now assign moderator roles to others.";
            }


            default -> {
                this.title = "New Notification";
                this.message = "You have a new notification.";
            }
        }
    }



    public ENotificationCategory getCategoryByType(ENotificationType type) {
        return switch (type) {
            // Likes & Favorites
            case LIKE_POST, LIKE_COMMENT, LIKE_MUSIC,
                 FAVORITE_POST, FAVORITE_MUSIC
                    -> ENotificationCategory.LIKES_FAVORITES;

            // Comments
            case COMMENT_ON_POST
                    -> ENotificationCategory.COMMENTS;

            // Follow Updates
            case FOLLOWEE_NEW_POST, FOLLOWEE_NEW_MUSIC, FOLLOWEE_NEW_PROFILE_PICTURE,
                 FOLLOWEE_NEW_VIDEO, FOLLOWEE_PROFILE_UPDATED
                    -> ENotificationCategory.FOLLOW_UPDATES;

            // Approved Follows
            case FOLLOW_REQUEST_ACCEPTED, FOLLOW_REQUEST_DECLINED
                    -> ENotificationCategory.APPROVED_FOLLOWS;

            // Follow Requests
            case FOLLOW_REQUEST_RECEIVED, FOLLOWER_REMOVED
                    -> ENotificationCategory.FOLLOW_REQUESTS;

            // Admin
            case ADMIN_WARNING_POST, ADMIN_WARNING_COMMENT,
                 ADMIN_DELETED_POST, ADMIN_DELETED_COMMENT,
                 ADMIN_PROMOTION_MANAGER, ADMIN_PROMOTION_SUPER_MANAGER
                    -> ENotificationCategory.ADMIN;
        };
    }

}

