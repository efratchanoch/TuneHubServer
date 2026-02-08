package com.example.tunehub.service;

import com.example.tunehub.dto.*;
import com.example.tunehub.model.*;
import com.example.tunehub.security.CustomUserDetails;
import com.example.tunehub.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    private final RoleRepository roleRepository;
    private final AIChatService aiChatService;
    private final InstrumentRepository instrumentRepository;
    private final TeacherRepository teacherRepository;
    private final AuthService authService;
    private final TeacherMapper teacherMapper;
    private final JwtUtils jwtUtils;
    private final InteractionService interactionService;


    @Autowired
    public UsersService(UsersRepository usersRepository,
                        UsersMapper usersMapper,
                        RoleRepository roleRepository,
                        AIChatService aiChatService,
                        InstrumentRepository instrumentRepository,
                        TeacherRepository teacherRepository,
                        AuthService authService, TeacherMapper teacherMapper, JwtUtils jwtUtils, InteractionService interactionService) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
        this.roleRepository = roleRepository;
        this.aiChatService = aiChatService;
        this.instrumentRepository = instrumentRepository;
        this.teacherRepository = teacherRepository;
        this.authService = authService;
        this.teacherMapper = teacherMapper;
        this.jwtUtils = jwtUtils;
        this.interactionService = interactionService;
    }

    public void setActive(Long userId, boolean active) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(active);
        usersRepository.save(user);
    }

    // Get
    public long countActiveUsers() {
        return usersRepository.countAllByIsActive(true);
    }

    public ResponseEntity<UsersProfileFullDTO> getUserById(Long id) {
        Users u = usersRepository.findUsersById(id);
        if (u == null) return ResponseEntity.notFound().build();

        UsersRatingUtils.calculateAndSetStarRating(u);
        UsersProfileFullDTO dto = usersMapper.usersToUsersProfileFullDTO(u);

        return ResponseEntity.ok(dto);
    }

//    public ResponseEntity<UsersUploadProfileImageDTO> getUsersProfileImageDTOById(Long id) throws IOException {
//        Users u = usersRepository.findUsersById(id);
//        if (u != null) {
//            UsersUploadProfileImageDTO dto = usersMapper.usersToDTO(u);
//            return ResponseEntity.ok(dto);
//        }
//        return ResponseEntity.notFound().build();
//    }


    public ResponseEntity<List<Users>> getUsers() {
        List<Users> u = usersRepository.findAll();
        return ResponseEntity.ok(u);
    }

    public ResponseEntity<List<UsersProfileDTO>> getUsersByTeacher(Long teacherId) {
        List<Users> u = usersRepository.findByTeacherId(teacherId);
        return ResponseEntity.ok(usersMapper.usersListToUsersProfileDTOList(u));
    }

    public ResponseEntity<Users> getUserByName(String name) {
        Users u = usersRepository.findUsersByName(name);
        if (u == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(u);
    }

    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByCity(String city) {
        List<Users> u = usersRepository.findAllByCity(city);
        return ResponseEntity.ok(usersMapper.usersToUsersMusiciansDTO(u));
    }

    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByCountry(String country) {
        List<Users> u = usersRepository.findAllByCountry(country);
        return ResponseEntity.ok(usersMapper.usersToUsersMusiciansDTO(u));
    }

    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByCreatedAt(String createdAt) {
        List<Users> u = usersRepository.findAllByCreatedAt(LocalDate.parse(createdAt));
        return ResponseEntity.ok(usersMapper.usersToUsersMusiciansDTO(u));
    }

    public ResponseEntity<UsersMusiciansDTO> getMusicianById(Long id) {
        Users u = usersRepository.findUsersById(id);
        if (u == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(usersMapper.usersToUsersMusiciansDTO(u));
    }

    public ResponseEntity<List<UsersMusiciansDTO>> getMusicians() {
        List<Users> u = usersRepository.findByUserType(EUserType.MUSICIAN);
        return ResponseEntity.ok(usersMapper.usersToUsersMusiciansDTO(u));
    }

    public ResponseEntity<List<UsersMusiciansDTO>> getMusiciansByName(String name) {
        List<Users> u = usersRepository.findAllByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(usersMapper.usersToUsersMusiciansDTO(u));
    }

    public ResponseEntity<List<UsersMusiciansDTO>> getUsersByUserType(List<EUserType> userTypes) {
        List<Users> users = usersRepository.findByUserTypeQuery(userTypes);
        return ResponseEntity.ok(usersMapper.usersToUsersMusiciansDTO(users));
    }

    // Put
    public ResponseEntity<UsersMusiciansDTO> updateUserType(Long userId, EUserType newType) {
        Users user = usersRepository.findById(userId).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();

        if (user.getUserTypes() == null) user.setUserTypes(new HashSet<>());
        if (!user.getUserTypes().contains(newType)) user.getUserTypes().add(newType);

        if (newType.equals(EUserType.MUSICIAN)) {
            user.getUserTypes().remove(EUserType.MUSIC_LOVER);
        }

        Users updatedUser = usersRepository.save(user);
        return ResponseEntity.ok(usersMapper.usersToUsersMusiciansDTO(updatedUser));
    }

//    public ResponseEntity<?> joinTeacher(Long studentId, Long teacherId) {
//        usersRepository.assignTeacherToStudent(studentId, teacherId);
//        return ResponseEntity.ok().build();
//    }

//    public ResponseEntity<?> joinTeacher(Long studentId) {
//        Long teacherId = authService.getCurrentUserId();
//        usersRepository.assignTeacherToStudent(studentId, teacherId);
//        return ResponseEntity.ok().build();
//    }

    @Transactional
    public void joinTeacher(Long studentId) {
        Long teacherId = authService.getCurrentUserId();
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        Users student = usersRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setTeacher(teacher);

        if (!student.getUserTypes().contains(EUserType.STUDENT)) {
            student.getUserTypes().add(EUserType.STUDENT);
        }

        usersRepository.save(student);
    }


    public ResponseEntity<UsersProfileDTO> updateUser(HttpServletRequest request,
                                                      HttpServletResponse response, Long id, String name, String email,
                                                      String city, String country, String description,
                                                      Boolean isActive, List<EUserType> userTypes,

                                                      String imageProfilePath, MultipartFile file) throws IOException {


        Users u = usersRepository.findUsersById(id);
        if (u == null) return ResponseEntity.notFound().build();


        if (name == null || name.trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Name is required"
            );
        }
        if (email == null || email.trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email is required"
            );
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(emailRegex)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid email format"
            );
        }

        boolean sensitiveChange =
                (file != null && !file.isEmpty()) ||
                        (name != null && !name.equals(u.getName()));

        String originalPasswordHash = u.getPassword();
        Users existingWithSameName = usersRepository.findUsersByName(name);
        if (existingWithSameName != null && !existingWithSameName.getId().equals(id))
            return ResponseEntity.status(HttpStatus.CONFLICT).build();


        u.setName(name);
        u.setEmail(email);
        if (isActive != null) u.setIsActive(isActive);
        if (userTypes != null) u.setUserTypes(new HashSet<>(userTypes));
        u.setCity(city);
        u.setCountry(country);
        u.setDescription(description);
        u.setEditedIn(LocalDateTime.now());
        u.setPassword(originalPasswordHash);

        if (file != null && !file.isEmpty()) {
            String uniqueFileName = FileUtils.generateUniqueFileName(file);
            FileUtils.uploadImage(file, uniqueFileName);
            u.setImageProfilePath(uniqueFileName);
        }

        Users updatedUser = usersRepository.save(u);

        if (sensitiveChange) {
            SecurityContextHolder.clearContext();

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            ResponseCookie cleanCookie = jwtUtils.getCleanJwtCookie();
            response.addHeader(HttpHeaders.SET_COOKIE, cleanCookie.toString());
            updatedUser.setIsActive(false);
        }
        return ResponseEntity.ok(usersMapper.usersToUsersProfileDTO(updatedUser));
    }

    // Post
    public ResponseEntity<?> signIn(UsersLogInDTO u, String jwtCookie, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        try {
            Users userFromDb = usersRepository.findByName(u.getName());
            if (userFromDb == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USER_NOT_FOUND");
            }

            if (jwtCookie != null && jwtUtils.validateJwtToken(jwtCookie)) {
                Long oldUserId = jwtUtils.getUserIdFromJwtToken(jwtCookie);
                setActive(oldUserId, false);
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(u.getName(), u.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            ResponseCookie jwtAccessCookie = jwtUtils.generateJwtCookie(userDetails);

            setActive(userFromDb.getId(), true);

            UsersProfileDTO profileDTO = usersMapper.usersToUsersProfileDTO(userFromDb);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtAccessCookie.toString())
                    .body(profileDTO);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("WRONG_PASSWORD");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<?> signUp(UsersSignUpDTO user, MultipartFile file) throws IOException {
        if (usersRepository.findByName(user.getName()) != null)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        if (file != null && !file.isEmpty()) {
            String uniqueFileName = FileUtils.generateUniqueFileName(file);
            FileUtils.uploadImage(file, uniqueFileName);
            user.setImageProfilePath(uniqueFileName);
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        Users us = usersMapper.usersSignUpDTOtoUsers(user);

        if (us.getUserTypes() == null || us.getUserTypes().isEmpty())
            us.setUserTypes(new HashSet<>(Collections.singletonList(EUserType.MUSIC_LOVER)));

        if (us.getRoles() == null) us.setRoles(new HashSet<>());

        Role roleUser = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        us.getRoles().add(roleUser);

        usersRepository.save(us);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    public ResponseEntity<Users> uploadImageProfile(MultipartFile file, Users p) throws IOException {
        String uniqueFileName = FileUtils.generateUniqueFileName(file);
        FileUtils.uploadImage(file, uniqueFileName);
        p.setImageProfilePath(uniqueFileName);
        Users users = usersRepository.save(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }


    public String getResponse(ChatRequest chatRequest) {
        try {
            return aiChatService.getResponse(chatRequest.message(), chatRequest.conversationId());
        } catch (Exception e) {
            e.printStackTrace();
            return "AI error: " + e.getMessage();
        }
    }

    public ResponseEntity<?> signUpAsTeacher(Long id, TeacherSignUpDTO teacherDetails) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        if (user.getCity() == null || user.getCountry() == null || user.getDescription() == null)
            return ResponseEntity.badRequest().body("City, Country, Description required");

        if (user.getUserTypes().contains(EUserType.TEACHER))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User is already a teacher");

        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setPricePerLesson(teacherDetails.getPricePerLesson());
        teacher.setExperience(teacherDetails.getExperience());
        teacher.setLessonDuration(teacherDetails.getLessonDuration());
        teacher.setDateUploaded(LocalDate.now());
        teacher.setInstruments(instrumentRepository.findAllById(teacherDetails.getInstrumentsIds()));
        teacherRepository.save(teacher);

        user.getUserTypes().add(EUserType.TEACHER);
        if (user.getRoles() == null) user.setRoles(new HashSet<>());
        user.getRoles().add(roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Teacher role not found.")));
        usersRepository.save(user);

        return ResponseEntity.ok("User successfully upgraded to Teacher.");
    }

    // Delete
    public ResponseEntity<Void> deleteUser(Long userId) {
        if (!usersRepository.existsById(userId)) return ResponseEntity.notFound().build();
        usersRepository.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    private boolean isOwnProfile(Users currentUser, Users profileUser) {
        return currentUser.getId().equals(profileUser.getId());
    }

    private boolean canBeMyStudent(Users currentUser, Users profileUser) {
        if (isOwnProfile(currentUser, profileUser)) return false;
        boolean currentUserIsTeacher = currentUser.getUserTypes().contains(EUserType.TEACHER);
        boolean profileIsStudent = profileUser.getTeacher() != null;
        return currentUserIsTeacher && !profileIsStudent;
    }

    private boolean isMyStudent(Users currentUser, Users profileUser) {
        if (!currentUser.getUserTypes().contains(EUserType.TEACHER) || profileUser.getTeacher() == null)
            return false;
        return profileUser.getTeacher().getId().equals(currentUser.getTeacherDetails().getId());
    }

    private boolean canEditOrDelete(Users currentUser, Users profileUser) {
        boolean currentUserIsSuperAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName() == ERole.ROLE_SUPER_ADMIN);
        boolean profileIsAdmin = profileUser.getRoles().stream()
                .anyMatch(r -> r.getName() == ERole.ROLE_SUPER_ADMIN);
        return currentUserIsSuperAdmin && !profileIsAdmin;
    }


    public UsersProfileCompleteDTO mapToProfileCompleteDTO(Users profileUser) {
        Users currentUser = authService.getCurrentUser();
        UsersProfileCompleteDTO dto = usersMapper.usersToUsersProfileCompleteDTO(profileUser);

        dto.setOwnProfile(isOwnProfile(currentUser, profileUser));
        dto.setImageProfilePath(FileUtils.imageToBase64(profileUser.getImageProfilePath()));

        dto.setCanBeMyStudent(canBeMyStudent(currentUser, profileUser));
        dto.setMyStudent(isMyStudent(currentUser, profileUser));

        boolean editOrDelete = canEditOrDelete(currentUser, profileUser);
        dto.setCanEditRoles(editOrDelete);
        dto.setCanDelete(editOrDelete);

        if (profileUser.getUserTypes().contains(EUserType.TEACHER)) {
            dto.setTeacherDetails(teacherMapper.toTeacherListingDTO(profileUser.getTeacherDetails()));
        }

        dto.setTotalLikes(interactionService.getTotalLikesCountUser(profileUser.getId()));
        dto.setTotalHearts(interactionService.getTotalHeartsCountUser(profileUser.getId()));
        dto.setTotalCommentsWritten(interactionService.getTotalCommentsWrittenByUser(profileUser.getId()));
        dto.setTotalCommentsReceived(interactionService.getTotalCommentsOnUserContent(profileUser.getId()));

        return dto;
    }


}
