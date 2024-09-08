package com.infinbank.rest.controller;

import com.infinbank.rest.dto.CourseDto;
import com.infinbank.rest.dto.ErrorResponseDto;
import com.infinbank.rest.dto.StudentDto;
import com.infinbank.rest.dto.TeacherDto;
import com.infinbank.rest.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/teachers", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping
    @Operation(
            summary = "Get the list of all teachers",
            description = "GET endpoint for teacher",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found the list of teachers",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = TeacherDto.class))}),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Something went wrong in server",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponseDto.class))
                            }
                    )
            }
    )
    public ResponseEntity<List<TeacherDto>> handleGetAll() {
        return ResponseEntity.ok()
                .body(teacherService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a teacher by id",
            description = "GET endpoint for getting teacher by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found a teacher",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = TeacherDto.class))}),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid id supplied",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "5XX",
                            description = "Something went wrong in server",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponseDto.class))
                            }
                    )
            }
    )
    public ResponseEntity<TeacherDto> handleGetById(@PathVariable Integer id) {
        TeacherDto teacherDto = teacherService.findById(id);
        return ResponseEntity.ok().body(teacherDto);
    }

    @GetMapping("/{id}/courses")
    @Operation(
            summary = "Get course of a particular teacher",
            description = "GET endpoint for getting course of a teacher",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Course of a teacher",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CourseDto.class))}),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid id supplied",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Teacher not found",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "5XX",
                            description = "Something went wrong in server",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponseDto.class))
                            }
                    )
            }
    )
    public ResponseEntity<CourseDto> handleGetCourseOfTeacher(@PathVariable Integer id) {
        Optional<CourseDto> courseDto = teacherService.findCourseOfTeacher(id);
        return courseDto
                .map(dto -> ResponseEntity.ok().body(dto))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}/students")
    @Operation(
            summary = "Get students of a particular teacher",
            description = "GET endpoint for getting students of a teacher",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Students of a teacher",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = StudentDto.class))}),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid id supplied",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Teacher not found",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "5XX",
                            description = "Something went wrong in server",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponseDto.class))
                            }
                    )
            }
    )
    public ResponseEntity<List<StudentDto>> handleGetStudentsOfTeacher(@PathVariable Integer id) {
        List<StudentDto> students = teacherService.findStudentsOfTeacher(id);
        return ResponseEntity.ok().body(students);
    }

    @PostMapping
    @Operation(
            summary = "Save Teacher",
            description = "Saving a teacher",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Teacher created",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = TeacherDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data supplied",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponseDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "5XX",
                            description = "Something went wrong in server",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponseDto.class))
                            }
                    )
            }
    )
    public ResponseEntity<TeacherDto> handelCreate(@Valid @RequestBody TeacherDto teacherDto,
                                                   UriComponentsBuilder uriBuilder) {
        TeacherDto savedTeacher = teacherService.save(teacherDto);
        return ResponseEntity.created(uriBuilder
                        .path("/api/teachers/{id}")
                        .build(Map.of("id", savedTeacher.getId())))
                .body(savedTeacher);
    }

    @PostMapping("/{teacherId}/courses/{courseId}")
    @Operation(
            summary = "Assign course to a teacher",
            description = "Assign course to a teacher by their id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Course assigned to teacher",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Teacher not found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponseDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Course not found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponseDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Course is already assigned to the teacher supplied",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "5XX",
                            description = "Something went wrong in server",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponseDto.class))
                            }
                    )
            }
    )
    public ResponseEntity<?> handleAssigningCourseToTeacher(@PathVariable Integer teacherId, @PathVariable Integer courseId) {
        boolean isAssigned = teacherService.assignCourseToTeacher(courseId, teacherId);
        if (!isAssigned) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Teacher cannot be assigned to two subjects");
        }
        return ResponseEntity.ok().body("Teacher is assigned to the course");
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update Teacher",
            description = "Updating a Teacher",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Teacher updated",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = TeacherDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data supplied",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponseDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Teacher not found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponseDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "5XX",
                            description = "Something went wrong in server",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponseDto.class))
                            }
                    )
            }
    )
    public ResponseEntity<TeacherDto> handleUpdate(@PathVariable Integer id, @RequestBody TeacherDto teacherDto) {
        TeacherDto updatedTeacherDto = teacherService.update(id, teacherDto);
        return ResponseEntity.ok().body(updatedTeacherDto);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Teacher",
            description = "Deleting a Teacher",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Teacher deleted",
                            content = {
                                    @Content()
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid id supplied",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponseDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Teacher not found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponseDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "5XX",
                            description = "Something went wrong in server",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponseDto.class))
                            }
                    )
            }
    )
    public ResponseEntity<?> handleDelete(@PathVariable Integer id) {
        teacherService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{teacherId}/courses/{courseId}")
    @Operation(
            summary = "Remove course from the teacher",
            description = "Removing a course from teacher",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Course removed from Teacher",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data supplied",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponseDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Teacher not found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponseDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Course not found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "5XX",
                            description = "Something went wrong in server",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponseDto.class))
                            }
                    )
            }
    )
    public ResponseEntity<?> handleRemoveCourseFromTeacher(@PathVariable Integer teacherId, @PathVariable Integer courseId) {
        boolean isRemoved = teacherService.removeCourseFromTeacher(courseId, teacherId);
        if (!isRemoved) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Teacher doesn't teach this subject");
        }
        return ResponseEntity.ok().body("Course is removed from the teacher");
    }
}
