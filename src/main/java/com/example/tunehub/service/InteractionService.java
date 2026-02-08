package com.example.tunehub.service;

import com.example.tunehub.dto.FavoriteItemDTO;
import com.example.tunehub.dto.NotificationSimpleDTO;
import com.example.tunehub.dto.PostResponseDTO;
import com.example.tunehub.dto.SheetMusicResponseDTO;
import com.example.tunehub.model.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InteractionService {

    private final PostRepository postRepository;
    private final SheetMusicRepository sheetMusicRepository;
    private final UsersRepository usersRepository;
    private final LikeRepository likeRepository;
    private final NotificationRepository notificationRepository;
    private final FavoriteRepository favoriteRepository;
    private final FollowRepository followRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;
    //  private final NotificationService notificationService;
    private final SheetMusicMapper sheetMusicMapper;
    private final PostMapper postMapper;

    @Autowired
    public InteractionService(PostRepository postRepository, SheetMusicRepository sheetMusicRepository,
                              UsersRepository usersRepository, LikeRepository likeRepository,
                              NotificationRepository notificationRepository, FavoriteRepository favoriteRepository,
                              FollowRepository followRepository, AuthService authService,
                              CommentRepository commentRepository,
                              //NotificationService notificationService,
                              SheetMusicMapper sheetMusicMapper,
                              PostMapper postMapper) {

        this.postRepository = postRepository;
        this.sheetMusicRepository = sheetMusicRepository;
        this.usersRepository = usersRepository;
        this.likeRepository = likeRepository;
        this.notificationRepository = notificationRepository;
        this.favoriteRepository = favoriteRepository;
        this.followRepository = followRepository;
        this.authService = authService;
        this.commentRepository = commentRepository;
        //    this.notificationService = notificationService;
        this.sheetMusicMapper = sheetMusicMapper;
        this.postMapper = postMapper;

    }


    private Users getContentOwner(ETargetType targetType, Long targetId) {
        switch (targetType) {
            case POST:
                return postRepository.findById(targetId)
                        .map(Post::getUser)
                        .orElse(null);

            case SHEET_MUSIC:
                return sheetMusicRepository.findById(targetId)
                        .map(SheetMusic::getUser)
                        .orElse(null);

            case USER:
                return usersRepository.findById(targetId).orElse(null);

            case COMMENT:
                return commentRepository.findById(targetId)
                        .map(Comment::getUser)
                        .orElse(null);
            default:
                return null;
        }
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

    public ResponseEntity<?> addLike(ETargetType targetType, Long targetId) {
        Long currentUserId = authService.getCurrentUserId();

        try {
            if (likeRepository.existsByUserIdAndTargetTypeAndTargetId(currentUserId, targetType, targetId)) {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }

            Like newLike = new Like(currentUserId, targetType, targetId);
            likeRepository.save(newLike);

            int newCount = updateLikesAndNotify(targetType, targetId);

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

        Users contentOwner = usersRepository.findUsersById(targetUserId);

        if (contentOwner == null) {
            return new ResponseEntity<>(EFollowStatus.NONE, HttpStatus.BAD_REQUEST);
        }

        if (existingFollow != null) {
            followRepository.delete(existingFollow);
            followRepository.flush();
            //  notificationService.handleUnfollowNotification(follower, contentOwner);
        }

        Follow newFollow = new Follow(follower.getId(), targetUserId, EFollowStatus.PENDING);
        followRepository.save(newFollow);
        followRepository.flush();

        //       notificationService.handleFollowRequestNotification(follower, contentOwner);

        return new ResponseEntity<>(EFollowStatus.PENDING, HttpStatus.ACCEPTED);
    }


    public ResponseEntity<EFollowStatus> getFollowStatus(Long targetUserId) {
        Users currentUser = authService.getCurrentUser();
        if (currentUser == null)
            return new ResponseEntity<>(EFollowStatus.NONE, HttpStatus.UNAUTHORIZED);

        if (currentUser.getId() == targetUserId)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        Follow follow = followRepository.findByFollowerIdAndFollowingId(
                currentUser.getId(), targetUserId);

        return new ResponseEntity<>(
                follow == null ? EFollowStatus.NONE : follow.getStatus(),
                HttpStatus.OK
        );
    }

    public ResponseEntity<?> approveFollow(Long followerId) {
        return approveOrRejectFollow(followerId, ENotificationType.FOLLOW_REQUEST_ACCEPTED);
    }

    public ResponseEntity<?> RejectFollow(Long followerId) {
        return approveOrRejectFollow(followerId, ENotificationType.FOLLOW_REQUEST_RECEIVED);
    }

    private ResponseEntity<?> approveOrRejectFollow(Long followerId, ENotificationType notify) {
        Users followingUser = authService.getCurrentUser();

        try {
            Optional<Follow> existingFollow = followRepository
                    .findByFollowerIdAndFollowingIdAndStatus(
                            followerId, followingUser.getId(), EFollowStatus.PENDING);

            if (existingFollow.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No pending follow request found.");
            }

            Follow follow = existingFollow.get();
            follow.setStatus(EFollowStatus.APPROVED);
            followRepository.save(follow);

            Users follower = usersRepository.findById(followerId).orElse(null);
            if (follower == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Follower user not found.");
            }

            //notificationService.handleFollowRequestDecisions(follower, followingUser, notify);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Follow request processed successfully.");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred.");
        }
    }


    // Favorites
    @Transactional
    public ResponseEntity<NotificationSimpleDTO> addFavorite(ETargetType targetType, Long targetId) {
        Long currentUserId = authService.getCurrentUserId();

        try {
            if (!favoriteRepository.existsByUserIdAndTargetTypeAndTargetId(currentUserId, targetType, targetId)) {

                Favorite newFavorite = new Favorite(currentUserId, targetType, targetId);
                favoriteRepository.save(newFavorite);

//                Users contentOwner = getContentOwner(targetType, targetId);
//                if (contentOwner != null) {
//                    Users currentUserEntity = authService.getCurrentUser();
//                    notificationRepository.save(new Notification(
//                            ENotificationType.FAVORITE_MUSIC,
//                            contentOwner,
//                            currentUserEntity,
//                            targetType,
//                            targetId
//                    ));
//                }
            }

            int newCount = favoriteRepository.countByTargetTypeAndTargetId(targetType, targetId);
            updateContentCount(targetType, targetId, newCount, false);
            return new ResponseEntity<>(new NotificationSimpleDTO(targetId, newCount, false), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<NotificationSimpleDTO> removeFavorite(ETargetType targetType, Long targetId) {
        Long currentUserId = authService.getCurrentUserId();

        try {
            Optional<Favorite> existingFavorite = favoriteRepository.findByUserIdAndTargetTypeAndTargetId(
                    currentUserId, targetType, targetId);

            existingFavorite.ifPresent(favoriteRepository::delete);

            int newCount = favoriteRepository.countByTargetTypeAndTargetId(targetType, targetId);
            updateContentCount(targetType, targetId, newCount, false);
            return new ResponseEntity<>(new NotificationSimpleDTO(targetId, newCount, false), HttpStatus.OK);

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
