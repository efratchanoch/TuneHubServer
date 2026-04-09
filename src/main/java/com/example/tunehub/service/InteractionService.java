package com.example.tunehub.service;

import com.example.tunehub.dto.common.FavoriteItemDTO;
import com.example.tunehub.dto.notification.NotificationEvent;
import com.example.tunehub.dto.post.PostResponseDTO;
import com.example.tunehub.dto.sheetmusic.SheetMusicResponseDTO;
import com.example.tunehub.mapper.PostMapper;
import com.example.tunehub.mapper.SheetMusicMapper;
import com.example.tunehub.model.*;
import com.example.tunehub.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InteractionService {

    private final PostRepository postRepository;
    private final SheetMusicRepository sheetMusicRepository;
    private final UsersRepository usersRepository;
    private final LikeRepository likeRepository;
    private final FavoriteRepository favoriteRepository;
    private final FollowRepository followRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final SheetMusicMapper sheetMusicMapper;
    private final PostMapper postMapper;
    private RabbitTemplate rabbitTemplate;

    @Value("${tunehub.rabbitmq.routingkey}")
    private String routingKey;

    @Value("${tunehub.rabbitmq.exchange}")
    private String exchangeName;

    @Autowired
    public InteractionService(PostRepository postRepository, SheetMusicRepository sheetMusicRepository, UsersRepository usersRepository, LikeRepository likeRepository, FavoriteRepository favoriteRepository, FollowRepository followRepository, AuthService authService, CommentRepository commentRepository, SheetMusicMapper sheetMusicMapper, PostMapper postMapper, RabbitTemplate rabbitTemplate) {
        this.postRepository = postRepository;
        this.sheetMusicRepository = sheetMusicRepository;
        this.usersRepository = usersRepository;
        this.likeRepository = likeRepository;
        this.favoriteRepository = favoriteRepository;
        this.followRepository = followRepository;
        this.authService = authService;
        this.commentRepository = commentRepository;
        this.sheetMusicMapper = sheetMusicMapper;
        this.postMapper = postMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Generates professional notification headers and content.
     * * @param targetType The entity being interacted with (POST, SHEET_MUSIC, COMMENT)
     *
     * @param actionType The type of interaction (LIKE, FAVORITE)
     * @param count      The current total count from the respective repository
     * @return A Map containing "title" and "content"
     */
    public Map<String, String> getNotificationMetadata(String targetType, String actionType, int count) {
        Map<String, String> metadata = new HashMap<>();
        String title = "";
        String content = "";

        String peopleStr = (count == 1) ? "person" : "people";
        String actionWord = "LIKE".equalsIgnoreCase(actionType) ? "liked" : "bookmarked";

        switch (targetType.toUpperCase()) {
            case "POST":
                title = "Post Interaction";
                content = String.format("%d %s %s your post", count, peopleStr, actionWord);
                break;

            case "SHEET_MUSIC":
                title = "Sheet Music Update";
                content = String.format("%d %s %s your sheet music", count, peopleStr, actionWord);
                break;

            case "COMMENT":
                title = "Comment Feedback";
                content = String.format("%d %s liked your comment", count, peopleStr);
                break;

            default:
                title = "New Notification";
                content = "You have a new interaction on your content";
                break;
        }

        metadata.put("title", title);
        metadata.put("content", content);
        return metadata;
    }

    /**
     * Generic dispatcher for all notification events across the TuneHub platform.
     * Ensures consistent communication with the Node.js Notification Microservice.
     */
    private void sendNotification(NotificationEvent event) {
        // Dispatching to RabbitMQ
        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, event);
        } catch (Exception e) {
            // Fallback or log if the queue is unreachable
            System.err.println("Failed to dispatch notification to RabbitMQ: " + e.getMessage());
        }
    }

    public void setAndSendNotification(Users recipient, Long senderId, ETargetType targetType,
                                              Map<String, String> metadata, String type, Long entityId, int newCount) {
        // Safety check: Don't notify the user about their own actions
        if (recipient == null) {
            return;
        }

        // Creating the DTO instance based on specific NotificationEvent class
        NotificationEvent event = new NotificationEvent();
        event.setRecipientId(recipient.getId());
        event.setSenderId(senderId);
        event.setTargetType(targetType);
        event.setTitle(metadata.get("title"));
        event.setContent(metadata.get("content"));
        event.setType(type);
        event.setEntityId(entityId);
        event.setCount(newCount);

        sendNotification(event);
    }

    /**
     * Resolves the owner of the target content based on the target type and ID.
     *
     * @param targetType The category of the content (POST, COMMENT, etc.)
     * @param targetId   The unique identifier of the content
     * @return The Users entity representing the owner, or null if not found
     */
    private Users getContentOwner(ETargetType targetType, Long targetId) {
        return switch (targetType) {
            case POST -> postRepository.findById(targetId)
                    .map(Post::getUser)
                    .orElse(null);

            case SHEET_MUSIC -> sheetMusicRepository.findById(targetId)
                    .map(SheetMusic::getUser)
                    .orElse(null);

            case COMMENT -> commentRepository.findById(targetId)
                    .map(Comment::getUser)
                    .orElse(null);

            case USER -> usersRepository.findById(targetId)
                    .orElse(null);

            default -> throw new IllegalArgumentException("Unsupported target type: " + targetType);
        };
    }

    private void updateContentCount(ETargetType targetType, Long targetId, int newCount, boolean isLike) {
        switch (targetType) {
            case POST:
                if (isLike) {
                    postRepository.updateLikeCount(targetId, newCount);
                }
                postRepository.updateFavoriteCount(targetId, newCount);
                break;

            case SHEET_MUSIC:
                if (isLike) {
                    sheetMusicRepository.updateLikeCount(targetId, newCount);
                }
                sheetMusicRepository.updateFavoriteCount(targetId, newCount);
                break;

            case COMMENT:
                commentRepository.updateLikeCount(targetId, newCount);
                break;

            default:
                throw new IllegalArgumentException("Unsupported ETargetType: " + targetType);
        }
    }

    // Likes
    private int updateLikesAndNotify(ETargetType targetType, Long targetId) {
        int newCount = likeRepository.countByTargetTypeAndTargetId(targetType, targetId);
        updateContentCount(targetType, targetId, newCount, true);
//        Users contentOwner = getContentOwner(targetType, targetId);
//        if (contentOwner != null) {
//            notificationService.handleLikeNotification(targetType, targetId, contentOwner, newCount);
//        }
        return newCount;
    }


    /**
     * Adds a like to a target entity and notifies the owner.
     *
     * @param targetType The type of entity (e.g., POST, SHEET_MUSIC, COMMENT)
     * @param targetId   The ID of the entity
     * @return ResponseEntity confirming the operation
     */
    public ResponseEntity<?> addLike(ETargetType targetType, Long targetId) {
        Long currentUserId = authService.getCurrentUserId();

        try {
            if (likeRepository.existsByUserIdAndTargetTypeAndTargetId(currentUserId, targetType, targetId)) {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }

            // Get content owner
            Users owner = getContentOwner(targetType, targetId);
            if (owner == null) {
                return new ResponseEntity<>("Content owner not found", HttpStatus.NOT_FOUND);
            }

            // Save the like
            Like newLike = new Like(currentUserId, targetType, targetId);
            likeRepository.save(newLike);
            int newCount = updateLikesAndNotify(targetType, targetId);

            setAndSendNotification(owner, currentUserId, targetType,
                    getNotificationMetadata(targetType.toString(), "LIKE", newCount),
                    "LIKE_" + targetType, targetId, newCount);

            return new ResponseEntity<>(newCount, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> removeLike(ETargetType targetType, Long targetId) {
        Long currentUserId = authService.getCurrentUserId();

        try {
            Optional<Like> existingLike = likeRepository.findByUserIdAndTargetTypeAndTargetId(
                    currentUserId, targetType, targetId);

            existingLike.ifPresent(likeRepository::delete);

            int newCount = updateLikesAndNotify(targetType, targetId);

            // Get content owner
            Users owner = getContentOwner(targetType, targetId);
            if (owner == null) {
                return new ResponseEntity<>("Content owner not found", HttpStatus.NOT_FOUND);
            }

            setAndSendNotification(owner, currentUserId, targetType,
                    getNotificationMetadata(targetType.toString(), "LIKE", newCount),
                    "LIKE_" + targetType, targetId, newCount);

            return new ResponseEntity<>(newCount, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Follow
    @Transactional
    public ResponseEntity<EFollowStatus> toggleFollowRequest(Long targetUserId) {
        Users follower = authService.getCurrentUser();
        if (follower.getId().equals(targetUserId))
            return new ResponseEntity<>(EFollowStatus.NONE, HttpStatus.BAD_REQUEST);

        Follow existingFollow = followRepository.findByFollowerIdAndFollowingIdForUpdate(
                follower.getId(), targetUserId);

        Users targetUser = usersRepository.findUsersById(targetUserId);

        if (existingFollow != null) {
            followRepository.delete(existingFollow);
            return new ResponseEntity<>(EFollowStatus.NONE, HttpStatus.OK);
        }

        Follow newFollow = new Follow(follower.getId(), targetUserId, EFollowStatus.PENDING);
        followRepository.save(newFollow);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("title", "New follow request");
        metadata.put("content", follower.getName() + " want to follow you");

        setAndSendNotification(targetUser, follower.getId(), ETargetType.USER,
                metadata, "FOLLOW_REQUEST_RECEIVED", follower.getId(), 0);

        return new ResponseEntity<>(EFollowStatus.PENDING, HttpStatus.ACCEPTED);
    }


    public ResponseEntity<EFollowStatus> getFollowStatus(Long followerUserId, Long followingUserId) {
        if (followerUserId == followingUserId)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        Optional<Follow> follow = followRepository.findByFollowerIdAndFollowingId(
                followerUserId, followingUserId);

        return new ResponseEntity<>(
                follow == null ? EFollowStatus.NONE : follow.get().getStatus(),
                HttpStatus.OK
        );
    }

    public void approveFollow(Long followerId) {
        Users currentUser = authService.getCurrentUser();

        approveOrRejectFollow(followerId, EFollowStatus.APPROVED, ENotificationType.FOLLOW_REQUEST_ACCEPTED);

        // Notify
        notifyFollowRequestStatus(followerId, currentUser.getId(), currentUser.getName(), true);
    }

    public void rejectFollow(Long followerId) {
        Users currentUser = authService.getCurrentUser();

        approveOrRejectFollow(followerId, EFollowStatus.DENIED, ENotificationType.FOLLOW_REQUEST_RECEIVED);

        // Notify
        notifyFollowRequestStatus(followerId, currentUser.getId(), currentUser.getName(), false);
    }

    private ResponseEntity<?> approveOrRejectFollow(Long followerId, EFollowStatus newStatus, ENotificationType notify) {
        Long currentUserId = authService.getCurrentUserId();

        try {
            Optional<Follow> followOpt = followRepository.findByFollowerIdAndFollowingId(followerId, currentUserId);

            if (followOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found");
            }

            Follow follow = followOpt.get();
            follow.setStatus(newStatus);
            followRepository.save(follow);

            return ResponseEntity.ok("Status updated to " + newStatus);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Update failed");
        }
    }

    /**
     * Updates the follow request status and notifies the requester.
     * This method ensures the requester receives a clear notification about the outcome.
     *
     * @param followerId The ID of the user who sent the follow request.
     * @param ownerId    The ID of the user who is approving/rejecting the request.
     * @param ownerName  The display name of the owner to be included in the message.
     * @param isApproved Boolean flag to determine the content of the notification.
     */
    public void notifyFollowRequestStatus(Long followerId, Long ownerId, String ownerName, boolean isApproved) {
        NotificationEvent notification = new NotificationEvent();

        // Set the recipient to the person who originally sent the request
        notification.setRecipientId(followerId);

        // Set the sender to the person who responded to the request
        notification.setSenderId(ownerId);

        // Use a specific type for status updates to allow 'Upsert' logic in the Notification Service
        notification.setType("FOLLOW_REQUEST_STATUS_UPDATE");
        notification.setTargetType(ETargetType.USER);

        // Construct a dynamic message based on the decision
        String status = isApproved ? "approved" : "rejected";
        String message = String.format("%s has %s your follow request.", ownerName, status);

        notification.setContent(message);

        sendNotification(notification);
    }

    /**
     * Notifies all followers when a user uploads new content.
     * @param creatorId   The ID of the user who uploaded the content.
     * @param creatorName The name of the creator (for the notification message).
     * @param entityId    The ID of the new Post or Sheet Music.
     * @param contentType The type of content (e.g., "POST" or "SHEET_MUSIC").
     */
    public void notifyFollowersOnNewContent(Long creatorId, String creatorName, Long entityId, ETargetType targetType) {
        // Fetch all follower IDs for this creator
        List<Long> followerIds = followRepository.findAllFollowerIdsByFollowingId(creatorId);

        // Iterate and send a notification to each follower
        for (Long followerId : followerIds) {
            NotificationEvent event = new NotificationEvent();
            event.setRecipientId(followerId);
            event.setSenderId(creatorId);

            // Define the type so the Node.js service knows how to handle it
            event.setType(targetType == ETargetType.POST ? "NEW_POST_FROM_FOLLOWING" : "NEW_SHEET_MUSIC_FROM_FOLLOWING");
            event.setTargetType(targetType);
            event.setEntityId(entityId);

            String contentDisplayName = targetType == ETargetType.POST  ? "post" : "sheet music";
            event.setContent(String.format("%s uploaded a new %s. Check it out!", creatorName, contentDisplayName));

            // Send to RabbitMQ
            sendNotification(event);
        }
    }
    // Favorites
    @Transactional
    public ResponseEntity<?> addFavorite(ETargetType targetType, Long targetId) {
        Long currentUserId = authService.getCurrentUserId();

        try {
            if (!favoriteRepository.existsByUserIdAndTargetTypeAndTargetId(currentUserId, targetType, targetId)) {

                Favorite newFavorite = new Favorite(currentUserId, targetType, targetId);
                favoriteRepository.save(newFavorite);

            }
            // Get content owner
            Users owner = getContentOwner(targetType, targetId);
            if (owner == null) {
                return new ResponseEntity<>("Content owner not found", HttpStatus.NOT_FOUND);
            }

            int newCount = favoriteRepository.countByTargetTypeAndTargetId(targetType, targetId);

            updateContentCount(targetType, targetId, newCount, false);
            setAndSendNotification(owner, currentUserId, targetType,
                    getNotificationMetadata(targetType.toString(), "FAVORITE", newCount),
                    "FAVORITE_" + targetType, targetId, newCount);


            return new ResponseEntity<>(newCount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> removeFavorite(ETargetType targetType, Long targetId) {
        Long currentUserId = authService.getCurrentUserId();

        try {
            Optional<Favorite> existingFavorite = favoriteRepository.findByUserIdAndTargetTypeAndTargetId(
                    currentUserId, targetType, targetId);

            existingFavorite.ifPresent(favoriteRepository::delete);

            // Get content owner
            Users owner = getContentOwner(targetType, targetId);
            if (owner == null) {
                return new ResponseEntity<>("Content owner not found", HttpStatus.NOT_FOUND);
            }

            int newCount = favoriteRepository.countByTargetTypeAndTargetId(targetType, targetId);
            updateContentCount(targetType, targetId, newCount, false);

            updateContentCount(targetType, targetId, newCount, false);
            setAndSendNotification(owner, currentUserId, targetType,
                    getNotificationMetadata(targetType.toString(), "FAVORITE", newCount),
                    "FAVORITE_" + targetType, targetId, newCount);


            return new ResponseEntity<>(newCount, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<FavoriteItemDTO> getFavoritesForUserByType(Long userId, ETargetType type, String search) {
        List<Favorite> favoriteRecords = favoriteRepository.findByUserIdAndTargetType(userId, type);
        String lowerCaseSearch = search.toLowerCase();

        return favoriteRecords.stream()
                .map(record -> fetchDetailsAndFilter(record, lowerCaseSearch))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    private FavoriteItemDTO fetchDetailsAndFilter(Favorite record, String search) {

        Object details = null;
        boolean matchesSearch = true;

        FavoriteItemDTO dto = new FavoriteItemDTO();
        dto.setId(record.getId());
        dto.setTargetId(record.getTargetId());
        dto.setTargetType(record.getTargetType());
        dto.setCreatedAt(record.getCreatedAt());

        switch (record.getTargetType()) {

            case POST:
                Optional<Post> postOptional = postRepository.findById(record.getTargetId());
                if (postOptional.isPresent()) {
                    PostResponseDTO postDto = postMapper.postToPostResponseDTO(
                            postOptional.get(),
                            record.getUserId(),
                            this.likeRepository,
                            this.favoriteRepository
                    );
                    details = postDto;
                    if (!search.isEmpty()) {
                        String title = postDto.getTitle() != null ? postDto.getTitle().toLowerCase() : "";
                        if (!title.contains(search)) {
                            matchesSearch = false;
                        }
                    }
                }
                break;


            case SHEET_MUSIC:
                Optional<SheetMusic> sheetMusicOptional = sheetMusicRepository.findById(record.getTargetId());
                if (sheetMusicOptional.isPresent()) {
                    SheetMusicResponseDTO sheetMusicDto = sheetMusicMapper.sheetMusicToSheetMusicResponseDTO(
                            sheetMusicOptional.get(),
                            record.getUserId(),
                            this.likeRepository,
                            this.favoriteRepository
                    );

                    details = sheetMusicDto;
                    if (!search.isEmpty()) {
                        String title = sheetMusicDto.title() != null ? sheetMusicDto.title().toLowerCase() : "";
                        if (!title.contains(search)) {
                            matchesSearch = false;
                        }
                    }
                }
                break;

            default:
                return null;
        }

        if (details == null || !matchesSearch) {
            return null;
        }

        dto.setDetails(details);
        return dto;
    }


    public long getTotalLikesCountUser(Long userId) {
        long totalLikes = 0;
        totalLikes += postRepository.sumPostLikes(userId);
        totalLikes += sheetMusicRepository.sumSheetLikes(userId);
        totalLikes += commentRepository.sumCommentLikes(userId);
        return totalLikes;
    }

    public long getTotalHeartsCountUser(Long userId) {
        long totalHearts = 0;
        totalHearts += postRepository.sumPostHearts(userId);
        totalHearts += sheetMusicRepository.sumSheetHearts(userId);
        return totalHearts;
    }

    public long getTotalCommentsWrittenByUser(Long userId) {
        return commentRepository.countByUserId(userId);
    }

    public long getTotalCommentsOnUserContent(Long userId) {
        return commentRepository.countCommentsOnUserPosts(userId);
    }
}
