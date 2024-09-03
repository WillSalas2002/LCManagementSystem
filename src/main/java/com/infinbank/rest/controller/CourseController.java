package com.infinbank.rest.controller;

import com.infinbank.rest.dto.CourseDto;
import com.infinbank.rest.dto.ErrorResponseDto;
import com.infinbank.rest.dto.StudentDto;
import com.infinbank.rest.dto.TeacherDto;
import com.infinbank.rest.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
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

/**
 * REST controller for managing courses.
 * This controller provides endpoints for CRUD operations on courses and
 * related entities such as students and teachers.
 *
 * @author WILL SALAS
 */
@RestController
@RequestMapping(value = "/apis/courses", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    /**
     * Retrieves all courses.
     *
     * @return a {@link ResponseEntity} containing a list of all {@link CourseDto}.
     */
    @GetMapping
    @Operation(
            summary = "Get the list of all courses",
            description = "GET endpoint for courses",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found the list of courses",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CourseDto.class))}),
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

    public ResponseEntity<List<CourseDto>> handleGetAll() {
        return ResponseEntity.ok().body(courseService.findAll());
    }

    /**
     * Retrieves a course by its ID.
     *
     * <p>If the course with the specified ID is not found, an exception is thrown
     * in the service layer, which is then handled to return a proper error response.</p>
     *
     * @param id the ID of the course to retrieve.
     * @return a {@link ResponseEntity} containing the {@link CourseDto}.
     * @throws com.infinbank.rest.exception.EntityNotFoundException if the course is not found.
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get a course by id",
            description = "GET endpoint for getting course by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found a course",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CourseDto.class))}),
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
    public ResponseEntity<CourseDto> handleGetById(@PathVariable Integer id) {
        CourseDto courseDto = courseService.findById(id);
        return ResponseEntity.ok().body(courseDto);
    }

    /**
     * Retrieves all students associated with a specific course.
     *
     * @param id the ID of the course.
     * @return a {@link ResponseEntity} containing a list of {@link StudentDto}.
     * @throws com.infinbank.rest.exception.EntityNotFoundException if the course is not found.
     */
    @GetMapping("/{id}/students")
    @Operation(
            summary = "Get Students of a particular Course",
            description = "GET endpoint for getting students of a course",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Students of a course",
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
                            description = "Course not found",
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
    public ResponseEntity<List<StudentDto>> handleGettingStudentsOfCourse(@PathVariable Integer id) {
        List<StudentDto> studentsByCourseId = courseService.findStudentsByCourse(id);
        return ResponseEntity.ok().body(studentsByCourseId);
    }

    /**
     * Retrieves all teachers associated with a specific course.
     *
     * @param id the ID of the course.
     * @return a {@link ResponseEntity} containing a list of {@link TeacherDto}.
     * @throws com.infinbank.rest.exception.EntityNotFoundException if the course is not found.
     */
    @GetMapping("/{id}/teachers")
    @Operation(
            summary = "Get Teachers of a particular course",
            description = "GET endpoint for getting teachers of a course",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Teachers who teacher a course",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = TeacherDto.class))}),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid id supplied",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Course not found",
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
    public ResponseEntity<List<TeacherDto>> handleGettingTeachersOfCourse(@PathVariable Integer id) {
        List<TeacherDto> teachersByCourseId = courseService.findTeachersByCourse(id);
        return ResponseEntity.ok().body(teachersByCourseId);
    }

    /**
     * Creates a new course.
     *
     * @param courseDto  the {@link CourseDto} containing the details of the course to create.
     * @param uriBuilder the {@link UriComponentsBuilder} used to build the location URI.
     * @return a {@link ResponseEntity} containing the created {@link CourseDto}.
     * @throws org.springframework.web.bind.MethodArgumentNotValidException if the passed fields are not valid.
     */
    @PostMapping
    @Operation(
            summary = "Save Course",
            description = "Saving a Course",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Course created",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CourseDto.class)
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
    public ResponseEntity<CourseDto> handleCreate(@Valid @RequestBody CourseDto courseDto,
                                                   UriComponentsBuilder uriBuilder) {
        CourseDto savedCourseDto = courseService.save(courseDto);
        return ResponseEntity.created(uriBuilder
                        .path("/api/courses/{id}")
                        .build(Map.of("id", savedCourseDto.getId())))
                .body(savedCourseDto);
    }

    /**
     * Updates an existing course in database.
     *
     * @return a {@link ResponseEntity} containing an updated {@link CourseDto}.
     * @throws com.infinbank.rest.exception.EntityNotFoundException if the course is not found.
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update Course",
            description = "Updating a Course",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Course updated",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CourseDto.class)
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
                            description = "Course not found",
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
    public ResponseEntity<CourseDto> handleUpdate(@PathVariable Integer id, @RequestBody CourseDto courseDto) {
        CourseDto updatedCourseDto = courseService.update(id, courseDto);
        return ResponseEntity.ok().body(updatedCourseDto);
    }

    /**
     * Deletes a course in database by ID.
     *
     * @return a {@link ResponseEntity} containing an updated {@link CourseDto}.
     * @throws com.infinbank.rest.exception.EntityNotFoundException if the course is not found.
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Course",
            description = "Deleting a Course",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Course deleted",
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
                            description = "Course not found",
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
        courseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
