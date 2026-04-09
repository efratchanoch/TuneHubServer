package com.example.tunehub.dto.notification;

import java.io.Serializable;

import com.example.tunehub.model.ETargetType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing a notification event to be sent via RabbitMQ.
 * Implements Serializable to allow the object to be converted into a byte stream for the queue.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The ID of the user who will receive the notification */
    private Long recipientId;

    /** The ID of the user who triggered the action */
    private Long senderId;

    /** Short title for the notification (e.g., "New Like") */
    private String title;

    /** Detailed content describing the event */
    private String content;

    /** * The category type */
    private String type;

    @Enumerated(EnumType.STRING)
    private ETargetType targetType;

    /** The ID of the related entity (Song ID, Post ID, etc.) */
    private Long entityId;

    private int count;
}