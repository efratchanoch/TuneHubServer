//package com.example.tunehub.controller;
//
//import com.example.tunehub.model.ENotificationCategory;
//import com.example.tunehub.service.NotificationRepository;
//import com.example.tunehub.service.NotificationService;
//import com.example.tunehub.dto.NotificationResponseDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Page;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/notification")
//public class NotificationController {
//
//    private final NotificationService notificationService;
//    private final NotificationRepository notificationRepository;
//
//    @Autowired
//    public NotificationController(NotificationService notificationService, NotificationRepository notificationRepository) {
//        this.notificationService = notificationService;
//        this.notificationRepository = notificationRepository;
//    }
//
//    @GetMapping
//    public ResponseEntity<Page<NotificationResponseDTO>> getNotifications(@PageableDefault(size = 20) Pageable pageable) {
//        try {
//            Page<NotificationResponseDTO> page = notificationService.getNotifications(pageable);
//            return ResponseEntity.ok(page);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(Page.empty());
//        }
//    }
//
//    @GetMapping("/unread-count")
//    public ResponseEntity<Long> unreadCount() {
//        try {
//            long count = notificationService.getUnreadCount();
//            return ResponseEntity.ok(count);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(0L);
//        }
//    }
//
//    @PostMapping("/{id}/read")
//    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
//        try {
//            notificationService.markAsRead(id);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(500).build();
//        }
//    }
//
//    @PostMapping("/read-all")
//    public ResponseEntity<Void> markAllAsRead() {
//        try {
//            notificationService.markAllAsRead();
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(500).build();
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        try {
//            notificationService.deleteNotification(id);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(500).build();
//        }
//    }
//
//    @GetMapping("/unreadByType")
//    public ResponseEntity<Map<String, Long>> getUnreadByCategory() {
//        try {
//            Map<String, Long> map = notificationService.getUnreadCountByType();
//            return ResponseEntity.ok(map);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(Map.of());
//        }
//    }
//
//    @GetMapping("/allNotificationsByCategory")
//    public ResponseEntity<Page<NotificationResponseDTO>> getAllNotificationsByCategory(
//            @PageableDefault(size = 20) Pageable pageable,
//            @RequestParam(required = false) ENotificationCategory category
//    ) {
//        try {
//            Page<NotificationResponseDTO> page = notificationService.getAllNotificationsByCategory(category, pageable);
//            return ResponseEntity.ok(page);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(Page.empty());
//        }
//    }
//
//    @GetMapping("/onlyUnreadNotificationsByCategory")
//    public ResponseEntity<Page<NotificationResponseDTO>> getOnlyUnreadNotificationsByCategory(
//            @PageableDefault(size = 20) Pageable pageable,
//            @RequestParam(required = false) ENotificationCategory category
//    ) {
//        try {
//            Page<NotificationResponseDTO> page = notificationService.getOnlyUnreadNotificationsByCategory(category, pageable);
//            return ResponseEntity.ok(page);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(Page.empty());
//        }
//    }
//
//}
