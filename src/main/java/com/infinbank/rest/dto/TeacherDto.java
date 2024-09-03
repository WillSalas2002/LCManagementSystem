package com.infinbank.rest.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for Teacher entity.
 * <p>
 * This class is used to transfer data between different layers of the application.
 * It contains fields for the teacher's ID and first, last, middle names and age.
 * </p>
 */
@Data
@Builder
public class TeacherDto {

    private Integer id;

    @NotNull(message = "firstName field is mandatory")
    @Size(min = 2, max = 100, message = "First Name should have at least 2 characters")
    private String firstName;

    @NotNull(message = "lastName field is mandatory")
    @Size(min = 2, max = 100, message = "Last Name should have at least 2 characters")
    private String lastName;

    @NotNull(message = "middleName field is mandatory")
    @Size(min = 2, max = 100, message = "Middle Name should have at least 2 characters")
    private String middleName;

    @NotNull(message = "Age is mandatory field")
    @Min(value = 5, message = "Age should not be less that 5")
    private Integer age;

    private CourseDto course;
}
