package com.example.tunehub.service;

import com.example.tunehub.dto.UsersMusiciansDTO;
import com.example.tunehub.dto.UsersProfileFullDTO;
import com.example.tunehub.model.EUserType;
import com.example.tunehub.model.SheetMusic;
import com.example.tunehub.model.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findUsersByName(String name);

    Users findUsersById(Long id);

    List<Users> findAllByNameContainingIgnoreCase(String name);

    List<Users> findTop5ByNameContainingIgnoreCaseAndUserTypesContaining(
            String name,
            EUserType userType
    );

    List<Users> findByTeacherId(Long teacherId);

    Users findByName(String name);

    @Query("SELECT u FROM Users u WHERE :userType MEMBER OF u.userTypes")
    List<Users> findByUserType(@Param("userType") EUserType userType);

    @Modifying
    @Query("UPDATE Users u SET u.followerCount = :count WHERE u.id = :id")
    @Transactional
    void updateFollowerCount(@Param("id") Long userId, @Param("count") int count);

    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.teacher t JOIN u.userTypes ut WHERE ut IN :userTypes")
    List<Users> findByUserTypeQuery(@Param("userTypes") List<EUserType> userTypes);

    List<Users> findAllByCountry(String country);

    List<Users> findAllByCity(String city);

    @Transactional
    @Modifying
    @Query("UPDATE Users u SET u.teacher.id = :teacherId WHERE u.id = :studentId")
    void assignTeacherToStudent(
            @Param("studentId") Long studentId,
            @Param("teacherId") Long teacherId
    );

    List<Users> findAllByCreatedAt(LocalDate createdAt);

    @Modifying
    @Query("UPDATE Users u SET u.isActive = :active WHERE u.id = :id")
    void updateActiveStatus(@Param("id") Long id, @Param("active") boolean active);

    long countAllByIsActive(boolean isActive);

}

