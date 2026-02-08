package com.example.tunehub.controller;

import com.example.tunehub.dto.*;
import com.example.tunehub.model.*;
import com.example.tunehub.security.jwt.JwtUtils;
import com.example.tunehub.service.AuthService;
import com.example.tunehub.service.UsersMapper;
import com.example.tunehub.service.UsersRepository;
import com.example.tunehub.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService usersService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AuthService authService;
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    @Autowired
    public UsersController(UsersService usersService,
                           AuthenticationManager authenticationManager,
                           JwtUtils jwtUtils, AuthService authService,
                           UsersRepository usersRepository, UsersMapper usersMapper) {
        this.usersService = usersService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.authService = authService;
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
    }

    // GET endpoints
    @GetMapping("/userById/{id}")
    public ResponseEntity<UsersProfileFullDTO> getUserById(@PathVariable Long id) {
        return usersService.getUserById(id);
    }

    @GetMapping("isOwn/{userId}")
    public ResponseEntity<Boolean> isOwnUser(@PathVariable Long userId) {
        Long currentUserId = authService.getCurrentUserId();
        boolean isOwn = currentUserId.equals(userId);
        return ResponseEntity.ok(isOwn);
    }

//        @GetMapping("/usersProfileImageDTOById/{id}")
//        public ResponseEntity<UsersUploadProfileImageDTO> getUsersProfileImageDTOById(@PathVariable Long id) throws IOException {
//            return usersService.getUsersProfileImageDTOById(id);
//        }

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getUsers() {
        return usersService.getUsers();
    }

    @GetMapping("/usersByTeacherId/{teacherId}")
    public ResponseEntity<List<UsersProfileDTO>> getUsersByTeacher(@PathVariable Long teacherId) {
        return usersService.getUsersByTeacher(teacherId);
    }

    @GetMapping("/userByName/{name}")
    public ResponseEntity<Users> getUserByName(@PathVariable String name) {
        return usersService.getUserByName(name);
    }

    @GetMapping("/userByCity/{city}")
    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByCity(@PathVariable String city) {
        return usersService.getUsersByCity(city);
    }

    @GetMapping("/userByCountry/{country}")
    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByCountry(@PathVariable String country) {
        return usersService.getUsersByCountry(country);
    }

    @GetMapping("/userByCreatedAt/{createdAt}")
    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByCreatedAt(@PathVariable String createdAt) {
        return usersService.getUsersByCreatedAt(createdAt);
    }

    @GetMapping("/musicianById/{id}")
    public ResponseEntity<UsersMusiciansDTO> getMusicianById(@PathVariable Long id) {
        return usersService.getMusicianById(id);
    }

    @GetMapping("/musicians")
    public ResponseEntity<List<UsersMusiciansDTO>> getMusicians() {
        return usersService.getMusicians();
    }

    @GetMapping("/musiciansByName/{name}")
    public ResponseEntity<List<UsersMusiciansDTO>> getMusiciansByName(@PathVariable String name) {
        return usersService.getMusiciansByName(name);
    }

    @GetMapping("/usersByUserType")
    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByUserType(@RequestParam List<EUserType> userTypes) {
        return usersService.getUsersByUserType(userTypes);
    }

    @GetMapping("/countActive")
    public ResponseEntity<Long> getActiveUsersCount() {
        return ResponseEntity.ok(usersService.countActiveUsers());
    }

    // PUT
    @PutMapping("/update-user-type/{userId}/{newType}")
    public ResponseEntity<UsersMusiciansDTO> updateUserType(@PathVariable Long userId, @PathVariable String newType) {
        EUserType type = EUserType.valueOf(newType.toUpperCase());
        return usersService.updateUserType(userId, type);
    }

//        @PutMapping("/joinTeacher/{studentId}/{teacherId}")
//        public ResponseEntity<?> joinTeacher(@PathVariable Long studentId, @PathVariable Long teacherId) {
//            return usersService.joinTeacher(studentId);
//        }

    @Transactional
    @PutMapping("/joinTeacher/{studentId}")
    public ResponseEntity<?> joinTeacher(@PathVariable Long studentId) {
        usersService.joinTeacher(studentId);
        return ResponseEntity.ok().build();
    }

//    @PutMapping("/updateUser/{id}")
//    public ResponseEntity<UsersProfileFullDTO> updateUser(
//            @PathVariable Long id,
//            @RequestParam String name,
//            @RequestParam String email,
//            @RequestParam(required = false) String city,
//            @RequestParam(required = false) String country,
//            @RequestParam(required = false) String description,
//            @RequestParam(required = false) Boolean isActive,
//            @RequestParam(required = false) List<EUserType> userTypes,
//            @RequestParam(required = false) String imageProfilePath,
//            @RequestPart(value = "image", required = false) MultipartFile file) throws IOException {
//        return usersService.updateUser(id, name, email, city, country, description, isActive, userTypes, imageProfilePath, file);
//    }

    // POST
    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody UsersLogInDTO u,
                                    @CookieValue(name = "jwt", required = false) String jwtCookie) {
        return usersService.signIn(u, jwtCookie, authenticationManager, jwtUtils);
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestPart(value = "image", required = false) MultipartFile file,
                                    @RequestPart("profile") UsersSignUpDTO user) throws IOException {
        return usersService.signUp(user, file);
    }

    @PostMapping("/signOut")
    public ResponseEntity<?> signOut() {
        usersService.setActive(authService.getCurrentUserId(), false);
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("You've been signed out!");
    }


    @PostMapping("/uploadImageProfile")
    public ResponseEntity<Users> uploadImageProfile(@RequestPart("image") MultipartFile file,
                                                    @RequestPart("profile") Users p) throws IOException {
        return usersService.uploadImageProfile(file, p);
    }


    @PostMapping("/chat")
    public String getResponse(@RequestBody ChatRequest chatRequest) {
        return usersService.getResponse(chatRequest);
    }

    @PostMapping("/signupTeacher/{id}")
    @Transactional
    public ResponseEntity<?> signUpAsTeacher(@PathVariable Long id,
                                             @RequestBody TeacherSignUpDTO teacherDetails) {
        return usersService.signUpAsTeacher(id, teacherDetails);
    }

    // DELETE endpoint
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        return usersService.deleteUser(userId);
    }

    @GetMapping("/profile-complete/{id}")
    public ResponseEntity<UsersProfileCompleteDTO> getProfileComplete(@PathVariable Long id) {
        Users profileUser = usersRepository.findUsersById(id);
        if (profileUser == null) return ResponseEntity.notFound().build();

        UsersProfileCompleteDTO dto = usersService.mapToProfileCompleteDTO(profileUser);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/me")
    public ResponseEntity<UsersProfileDTO> getCurrentUserProfileDTO() {
        Users currentUser = authService.getCurrentUser();
        UsersProfileDTO dto = usersMapper.usersToUsersProfileDTO(currentUser);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/currentUserMusicianDetails")
    public ResponseEntity<UsersMusiciansDTO> getCurrentUserMusicianDetails() {
        Users currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(usersMapper.usersToUsersMusiciansDTO(currentUser));
    }


    @PutMapping("/updateCurrentUser")
    public ResponseEntity<UsersProfileDTO> updateUser(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) List<EUserType> userTypes,
            @RequestParam(required = false) String imageProfilePath,
            @RequestPart(value = "image", required = false) MultipartFile file) throws IOException {
        return usersService.updateUser( request,  response,  authService.getCurrentUserId(), name, email, city, country, description, isActive, userTypes, imageProfilePath, file);
    }

    @PutMapping("/currentUserTypeToMusician")
    public ResponseEntity<Void> updateCurrentUserTypeToMusician() {
        Long currentUserId = authService.getCurrentUserId();
        usersService.updateUserType(currentUserId, EUserType.MUSICIAN);
        return ResponseEntity.ok().build();
    }

}
