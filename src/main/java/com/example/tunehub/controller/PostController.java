package com.example.tunehub.controller;

import com.example.tunehub.dto.PostResponseDTO;
import com.example.tunehub.dto.PostUploadDTO;
import com.example.tunehub.model.*;
import com.example.tunehub.service.*;
import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    private final LikeRepository likeRepository;
    private final FavoriteRepository favoriteRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final AuthService authService;
    private final CommentRepository commentRepository;
   // private final NotificationService notificationService;

    @Autowired
    public PostController(LikeRepository likeRepository, FavoriteRepository favoriteRepository, PostRepository postRepository, PostMapper postMapper, AuthService authService,
                          //NotificationService notificationService,
                          CommentRepository commentRepository) {
        this.likeRepository = likeRepository;
        this.favoriteRepository = favoriteRepository;
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.authService = authService;
      //  this.notificationService = notificationService;
        this.commentRepository= commentRepository;
    }


    //Get
    @GetMapping("/postById/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            Post p = postRepository.findPostById(id);
            if (p == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(postMapper.postToPostResponseDTO(p, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDTO>> getPosts() {
        try {
            Long currentUserId = authService.getCurrentUserId();

            List<Post> posts = postRepository.findAll();
            if (posts.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            for (Post post : posts) {
                List<Comment> comments = commentRepository.findByPostId(post.getId());
                double starRating = UsersRatingUtils.calculatePostStarRating(post, comments);
                post.setRating(starRating);
            }

            return new ResponseEntity<>(postMapper.postListToPostResponseDTOlist(posts, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/postsByUserId/{id}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByUserId(@PathVariable Long id) {
        try {
      Long currentUserId = authService.getCurrentUserId();

            List<Post> p = postRepository.findAllUserPosts(id);
            System.out.println("Backend returning " + p.size() + " posts for ID " + id);

            if (p == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(postMapper.postListToPostResponseDTOlist(p, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/newPosts")
    public ResponseEntity<List<PostResponseDTO>> getNewPosts() {
        try {
           Long currentUserId = authService.getCurrentUserId();
            List<Post> p = postRepository.findByDateUploaded(LocalDate.now());
            if (p == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(postMapper.postListToPostResponseDTOlist(p, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/postsByTitle/{title}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByName(@PathVariable String title) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            List<Post> p = postRepository.findAllTop5ByTitleContainingIgnoreCase(title);
            if (p.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(postMapper.postListToPostResponseDTOlist(p, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/postsByDate/{date}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByDate(@PathVariable LocalDate date) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            List<Post> p = postRepository.findByDateUploaded(date);
            if (p.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(postMapper.postListToPostResponseDTOlist(p, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/uploadPost")
    public ResponseEntity<PostResponseDTO> createPost(
            @RequestPart("data") PostUploadDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "audio", required = false) MultipartFile audio,
            @RequestPart(value = "video", required = false) MultipartFile video) {
        try {
            Users user = authService.getCurrentUser();
            Post post = postMapper.postUploadDTOtoPost(dto);
            post.setUser(user);

            if (images != null && !images.isEmpty()) {
                List<String> imageNames = new ArrayList<>();
                for (MultipartFile img : images) {
                    String uniqueName = FileUtils.generateUniqueFileName(img);
                    FileUtils.uploadImage(img, uniqueName);
                    imageNames.add(uniqueName);
                }
                post.setImagesPath(imageNames);
            }

            if (audio != null) {
                String uniqueAudioName = FileUtils.generateUniqueFileName(audio);
                FileUtils.uploadAudio(audio, uniqueAudioName);
                post.setAudioPath(uniqueAudioName);
            }

            if (video != null) {
                String uniqueVideoName = FileUtils.generateUniqueFileName(video);
                FileUtils.uploadVideo(video, uniqueVideoName);
                post.setVideoPath(uniqueVideoName);
            }

            postRepository.save(post);
            PostResponseDTO responseDTO = postMapper.postToPostResponseDTO(post, user.getId(), likeRepository, favoriteRepository);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/audio/{audioPath}")
    public ResponseEntity<Resource> getAudio(@PathVariable String audioPath) throws IOException {
        InputStreamResource resource = FileUtils.getAudio(audioPath);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + audioPath + "\"")
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(resource);
    }

    @GetMapping("/video/{videoPath}")
    public ResponseEntity<Resource> getVideo(@PathVariable String videoPath) throws IOException {
        InputStreamResource resource = FileUtils.getVideo(videoPath);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + videoPath + "\"")
                .contentType(MediaType.parseMediaType("video/mp4"))
                .body(resource);
    }

    @GetMapping("/image/{imagePath}")
    public ResponseEntity<Resource> getImage(@PathVariable String imagePath) throws IOException {
        InputStreamResource resource = new InputStreamResource(
                new ByteArrayInputStream(FileUtils.imageToBase64(imagePath).getBytes())
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imagePath + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }


    // Delete
    @DeleteMapping("/deletePostByPostId/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN' ,'ROLE_SUPER_ADMIN')")
    @Transactional
    public ResponseEntity<?> deletePostByPostId(@PathVariable Long id) {
        try {
            Post p = postRepository.findPostById(id);
            if (p == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Users postOwner = p.getUser();
            Users adminUser = authService.getCurrentUser();

            likeRepository.deleteAllByTargetTypeAndTargetId(ETargetType.POST, id);
            favoriteRepository.deleteAllByTargetTypeAndTargetId(ETargetType.POST, id);

//            notificationService.createNotification(
//                    ENotificationType.ADMIN_WARNING_POST,
//                    postOwner,
//                    adminUser,
//                    ETargetType.POST,
//                    id
//            );
            postRepository.deleteById(id);

            return new ResponseEntity<>("Post deleted and user notified.", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/admin/sendPostOwnerWarningNotification/{postId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN' ,'ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> sendPostOwnerWarningNotification(@PathVariable Long postId, @RequestParam(required = false) String customMessage) {
        try {
            Post p = postRepository.findPostById(postId);
            if (p == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            Users postOwner = p.getUser();
            Users adminUser = authService.getCurrentUser();

            Notification notification = new Notification(
                    ENotificationType.ADMIN_WARNING_POST,
                    postOwner,
                    adminUser,
                    ETargetType.POST,
                    postId
            );

            if (customMessage != null && !customMessage.trim().isEmpty()) {
                notification.setMessage(customMessage);
            } else {
                notification.setTitleAndMessageBasedOnType(ENotificationType.ADMIN_WARNING_POST, adminUser, 1);
            }

            //notificationService.saveNotification(notification);
            return new ResponseEntity<>("Warning notification sent to post owner.", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
