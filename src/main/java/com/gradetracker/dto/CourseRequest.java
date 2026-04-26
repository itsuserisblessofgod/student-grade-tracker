package com.gradetracker.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CourseRequest {

    @NotBlank(message = "Course name is required")
    private String name;

    private String description;

    @Min(value = 1, message = "Credits must be at least 1")
    private int credits;
}
