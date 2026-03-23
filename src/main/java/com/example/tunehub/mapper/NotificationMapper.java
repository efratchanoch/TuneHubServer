package com.example.tunehub.mapper;

import com.example.tunehub.dto.notification.NotificationFollowDTO;
import com.example.tunehub.dto.notification.NotificationResponseDTO;
import com.example.tunehub.dto.notification.NotificationLikesAndFavoritesDTO;
import com.example.tunehub.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UsersMapper.class)
public interface NotificationMapper {
    @Mapping(source = "read", target = "isRead")
    NotificationResponseDTO NotificationToNotificationResponseDTO(Notification n);

    @Mapping(source = "read", target = "read")
    NotificationLikesAndFavoritesDTO NotificationToNotificationLikesAndFavoritesDTO(Notification n);

    @Mapping(source = "read", target = "isRead")
    NotificationFollowDTO NotificationToNotificationFollowDTO(Notification n);
}
