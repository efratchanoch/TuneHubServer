package com.example.tunehub.model;

public enum ENotificationType {
    LIKE_POST(ENotificationCategory.LIKES_FAVORITES),
    LIKE_COMMENT(ENotificationCategory.LIKES_FAVORITES),
    LIKE_MUSIC(ENotificationCategory.LIKES_FAVORITES),
    FAVORITE_POST(ENotificationCategory.LIKES_FAVORITES),
    FAVORITE_MUSIC(ENotificationCategory.LIKES_FAVORITES),

    COMMENT_ON_POST(ENotificationCategory.COMMENTS),

    FOLLOWEE_NEW_POST(ENotificationCategory.FOLLOW_UPDATES),
    FOLLOWEE_NEW_MUSIC(ENotificationCategory.FOLLOW_UPDATES),
    FOLLOWEE_NEW_PROFILE_PICTURE(ENotificationCategory.FOLLOW_UPDATES),
    FOLLOWEE_NEW_VIDEO(ENotificationCategory.FOLLOW_UPDATES),
    FOLLOWEE_PROFILE_UPDATED(ENotificationCategory.FOLLOW_UPDATES),

    FOLLOW_REQUEST_ACCEPTED(ENotificationCategory.APPROVED_FOLLOWS),
    FOLLOW_REQUEST_DECLINED(ENotificationCategory.APPROVED_FOLLOWS),

    FOLLOW_REQUEST_RECEIVED(ENotificationCategory.FOLLOW_REQUESTS), // זו בקשת עוקב שמגיעה אליך
    FOLLOWER_REMOVED(ENotificationCategory.FOLLOW_UPDATES), // הסרת עוקב נכנסת כעדכון עוקב

    ADMIN_WARNING_POST(ENotificationCategory.ADMIN),
    ADMIN_WARNING_COMMENT(ENotificationCategory.ADMIN),
    ADMIN_DELETED_POST(ENotificationCategory.ADMIN),
    ADMIN_DELETED_COMMENT(ENotificationCategory.ADMIN),
    ADMIN_PROMOTION_MANAGER(ENotificationCategory.ADMIN),
    ADMIN_PROMOTION_SUPER_MANAGER(ENotificationCategory.ADMIN);

    private final ENotificationCategory category;

    ENotificationType(ENotificationCategory category) {
        this.category = category;
    }

    public ENotificationCategory getCategory() {
        return category;
    }
}