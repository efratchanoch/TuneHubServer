package com.example.tunehub.service;


import com.example.tunehub.model.Teacher;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findTeacherById(Long id);

    List<Teacher> findAllByLessonDuration(Double lessonDuration);

    List<Teacher> findAllByExperience(int experience);

    List<Teacher> findAllByInstrumentsId(Long instrument_id);
}
