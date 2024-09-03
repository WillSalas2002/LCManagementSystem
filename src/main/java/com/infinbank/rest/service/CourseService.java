package com.infinbank.rest.service;

import com.infinbank.rest.dto.CourseDto;
import com.infinbank.rest.dto.StudentDto;
import com.infinbank.rest.dto.TeacherDto;
import com.infinbank.rest.exception.CourseEntityNotFoundException;
import com.infinbank.rest.mapper.CourseMapper;
import com.infinbank.rest.mapper.StudentMapper;
import com.infinbank.rest.mapper.TeacherMapper;
import com.infinbank.rest.model.Course;
import com.infinbank.rest.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer responsible for managing courses and related entities such as students and teachers.
 * This service provides methods for CRUD operations and retrieving associated entities.
 *
 * <p>Transactional management is used to ensure the integrity of operations. Read-only
 * transactions are marked explicitly to optimize performance.</p>
 *
 * <p>Mapping between entities and DTOs is handled using mappers, which convert between the two
 * representations.</p>
 *
 * <p>Exceptions related to entity retrieval are handled within the global exception handler and propagated
 * as needed.</p>
 *
 * @see CourseDto
 * @see StudentDto
 * @see TeacherDto
 * @see CourseMapper
 * @see StudentMapper
 * @see TeacherMapper
 * @see CourseRepository
 * @see com.infinbank.rest.errorhandling.GlobalExceptionHandler
 */
@Service
@RequiredArgsConstructor
public class CourseService implements CrudService<CourseDto, Integer> {

    private final CourseMapper courseMapper;

    private final StudentMapper studentMapper;

    private final TeacherMapper teacherMapper;

    private final CourseRepository courseRepository;

    /**
     * Finds a course by its ID.
     *
     * <p>Throws an exception if the course is not found.</p>
     *
     * @param id the ID of the course to retrieve.
     * @return the {@link CourseDto} representing the course.
     * @throws com.infinbank.rest.exception.EntityNotFoundException if the course is not found.
     */
    @Transactional(readOnly = true)
    public CourseDto findById(Integer id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseEntityNotFoundException(id));
        return courseMapper.toDto(course);
    }

    /**
     * Retrieves all courses.
     *
     * @return a list of {@link CourseDto} representing all courses.
     */
    @Transactional(readOnly = true)
    public List<CourseDto> findAll() {
        List<Course> all = courseRepository.findAll();
        return all.stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Saves a new course.
     *
     * @param courseDto the {@link CourseDto} containing the details of the course to save.
     * @return the saved {@link CourseDto}.
     */
    @Transactional
    public CourseDto save(CourseDto courseDto) {
        Course course = courseMapper.toEntity(courseDto);
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toDto(savedCourse);
    }

    /**
     * Updates an existing course.
     *
     * <p>Throws an exception if the course to update is not found.</p>
     *
     * @param id the ID of the course to update.
     * @param updatedCourseDto the {@link CourseDto} containing the updated course details.
     * @return the updated {@link CourseDto}.
     * @throws com.infinbank.rest.exception.EntityNotFoundException if the course is not found.
     */
    @Transactional
    public CourseDto update(Integer id, CourseDto updatedCourseDto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseEntityNotFoundException(id));
        updateCourse(updatedCourseDto, course);
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toDto(savedCourse);
    }

    /**
     * Deletes a course by its ID.
     *
     * <p>Throws an exception if the course to delete is not found.</p>
     *
     * @param courseId the ID of the course to delete.
     * @throws com.infinbank.rest.exception.EntityNotFoundException if the course is not found.
     */
    @Transactional
    public void deleteById(Integer courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseEntityNotFoundException(courseId));
        courseRepository.delete(course);
    }

    /**
     * Retrieves all students associated with a specific course.
     *
     * @param courseId the ID of the course.
     * @return a list of {@link StudentDto} representing the students associated with the course.
     * @throws com.infinbank.rest.exception.EntityNotFoundException if the course is not found.
     */
    @Transactional(readOnly = true)
    public List<StudentDto> findStudentsByCourse(Integer courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseEntityNotFoundException(courseId));
        return course.getStudents().stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all teachers associated with a specific course.
     *
     * @param courseId the ID of the course.
     * @return a list of {@link TeacherDto} representing the teachers associated with the course.
     * @throws com.infinbank.rest.exception.EntityNotFoundException if the course is not found.
     */
    @Transactional(readOnly = true)
    public List<TeacherDto> findTeachersByCourse(Integer courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseEntityNotFoundException(courseId));
        return course.getTeachers().stream()
                .map(teacherMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates the details of a course entity with the provided {@link CourseDto}.
     *
     * @param updatedCourseDto the {@link CourseDto} containing the updated course details.
     * @param course the {@link Course} entity to update.
     */
    private static void updateCourse(CourseDto updatedCourseDto, Course course) {
        course.setCourseName(updatedCourseDto.getCourseName());
    }
}
