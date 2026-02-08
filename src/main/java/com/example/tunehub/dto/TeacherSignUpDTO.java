package com.example.tunehub.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class TeacherSignUpDTO {

    @NotNull(message = "Price per lesson is required.")
    @DecimalMin(value = "10.00", message = "Price must be at least 10.00")
    private double pricePerLesson;

    @NotNull(message = "Experience is required.")
    @Min(value = 0, message = "Experience must be a non-negative number.")
    private int experience;

    @NotNull(message = "Lesson duration is required.")
    @DecimalMin(value = "0.25", message = "Lesson duration must be at least 0.25 hours (15 minutes).")
    private double lessonDuration;

    @NotNull(message = "Instrument IDs list is required.")
    @Size(min = 1, message = "At least one instrument ID is required.")
    private List<Long> instrumentsIds;


    public double getPricePerLesson() {
        return pricePerLesson;
    }

    public void setPricePerLesson(double pricePerLesson) {
        this.pricePerLesson = pricePerLesson;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public double getLessonDuration() {
        return lessonDuration;
    }

    public void setLessonDuration(double lessonDuration) {
        this.lessonDuration = lessonDuration;
    }

    public List<Long> getInstrumentsIds() {
        return instrumentsIds;
    }

    public void setInstrumentsIds(List<Long> instrumentsIds) {
        this.instrumentsIds = instrumentsIds;
    }
}
