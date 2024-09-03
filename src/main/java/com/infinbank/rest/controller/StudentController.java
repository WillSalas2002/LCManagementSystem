package com.infinbank.rest.controller;

import com.infinbank.rest.dto.CourseDto;
import com.infinbank.rest.dto.ErrorResponseDto;
import com.infinbank.rest.dto.StudentDto;
import com.infinbank.rest.dto.TeacherDto;
import com.infinbank.rest.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RestController
@RequestMapping(value = "/apis/students", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Student")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    @Operation(
            summary = "Get the list of all students",
            description = "GET endpoint for student",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found the list of students",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = StudentDto.class))}),
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
    public ResponseEntity<List<StudentDto>> handleGetAll() {
        return ResponseEntity.ok().body(studentService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a student by id",
            description = "GET endpoint for getting student by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found a student",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = StudentDto.class))}),
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
    public ResponseEntity<StudentDto> handleGetById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(studentService.findById(id));
    }

    @GetMapping("/{id}/courses")
    @Operation(
            summary = "Get courses of a particular student",
            description = "GET endpoint for getting courses of a student",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Courses of a student",
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
                            description = "Student not found",
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
    public ResponseEntity<List<CourseDto>> handleGettingCoursesOfStudent(@PathVariable Integer id) {
        List<CourseDto> coursesByStudentId = studentService.findCoursesByStudentId(id);
        return ResponseEntity.ok().body(coursesByStudentId);
    }

    @GetMapping("/{id}/teachers")
    @Operation(
            summary = "Get teachers of a particular student",
            description = "GET endpoint for getting teachers of a student",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Teachers of a student",
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
                            description = "Student not found",
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
    public ResponseEntity<List<TeacherDto>> handleGettingTeachersOfStudent(@PathVariable Integer id) {
        List<TeacherDto> teachersByStudentId = studentService.findTeachersByStudentId(id);
        return ResponseEntity.ok().body(teachersByStudentId);
    }

    @PostMapping
    @Operation(
            summary = "Save Student",
            description = "Saving a student",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Student created",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = StudentDto.class)
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
    public ResponseEntity<StudentDto> handleCreate(@Valid @RequestBody StudentDto studentDto,
                                                   UriComponentsBuilder uriBuilder) {
        StudentDto savedStudent = studentService.save(studentDto);
        return ResponseEntity.created(uriBuilder
                        .path("/api/students/{id}")
                        .build(Map.of("id", savedStudent.getId())))
                .body(savedStudent);
    }

    @PostMapping("/{studentId}/teachers/{teacherId}")
    @Operation(
            summary = "Assign teacher to a student",
            description = "Assign teacher to a student by their id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Teacher assigned to student",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Student not found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = StudentDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Teacher not found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = TeacherDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Teacher is already assigned to the student supplied",
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
    public ResponseEntity<?> handleAssigningTeacherToStudent(@PathVariable("studentId") Integer studentId,
                                                             @PathVariable("teacherId") Integer teacherId) {
        boolean isAssigned = studentService.assignTeacherToStudent(teacherId, studentId);
        if (!isAssigned) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Teacher already assigned to this student");
        }
        return ResponseEntity.ok().body("Teacher assigned to student");
    }

    @PostMapping("/{studentId}/courses/{courseId}")
    @Operation(
            summary = "Assign course to a student",
            description = "Assign course to a student by their id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Course assigned to student",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Student not found",
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
                            description = "Course is already assigned to the student supplied",
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
    public ResponseEntity<?> handleAssigningCourseToStudent(@PathVariable("studentId") Integer studentId,
                                                            @PathVariable("courseId") Integer courseId) {
        boolean isAssigned = studentService.assignCourseToStudent(courseId, studentId);
        if (!isAssigned) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Course already assigned to this student");
        }
        return ResponseEntity.ok().body("Course assigned to student");
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update Student",
            description = "Updating a student",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Student updated",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = StudentDto.class)
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
                            description = "Student not found",
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
    public ResponseEntity<StudentDto> handleUpdate(@PathVariable Integer id, @RequestBody StudentDto studentDto) {
        StudentDto updatedStudentDto = studentService.update(id, studentDto);
        return ResponseEntity.ok().body(updatedStudentDto);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Student",
            description = "Deleting a student",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Student deleted",
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
                            description = "Student not found",
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
        studentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{studentId}/teachers/{teacherId}")
    @Operation(
            summary = "Remove teacher from the list of teachers of a student",
            description = "Removing a teacher from student",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Teacher removed from Student",
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
                            description = "Student not found",
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
    public ResponseEntity<?> handleRemovingTeacherFromStudent(@PathVariable("studentId") Integer studentId,
                                                              @PathVariable("teacherId") Integer teacherId) {
        boolean isRemoved = studentService.removeTeacherFromStudent(teacherId, studentId);
        if (!isRemoved) {
            ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Teacher not found in the list of student's teachers");
        }
        return ResponseEntity.ok().body("Teacher removed from student");
    }

    @DeleteMapping("/{studentId}/courses/{courseId}")
    @Operation(
            summary = "Remove course from the list of courses of a student",
            description = "Removing a course from student",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Course removed from Student",
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
                            description = "Student not found",
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
    public ResponseEntity<?> handleRemovingCourseFromStudent(@PathVariable("studentId") Integer studentId,
                                                             @PathVariable("courseId") Integer courseId) {
        boolean isRemoved = studentService.removeCourseFromStudent(courseId, studentId);
        if (!isRemoved) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Course not found in the list of student's courses");
        }
        return ResponseEntity.ok().body("Course removed from student");
    }
}
