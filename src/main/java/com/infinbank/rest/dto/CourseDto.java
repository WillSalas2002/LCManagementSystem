package com.infinbank.rest.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for Course entity.
 * <p>
 * This class is used to transfer data between different layers of the application.
 * It contains fields for the course's ID and course name.
 * </p>
 */
@Data
@Builder
public class CourseDto {

    private Integer id;

    @NotNull(message = "Course title is mandatory")
    @Size(min = 3, max = 100, message = "Size should not be less that 3 symbols")
    private String courseName;
}
