package com.infinbank.rest.service;

import com.infinbank.rest.dto.CourseDto;
import com.infinbank.rest.dto.StudentDto;
import com.infinbank.rest.dto.TeacherDto;
import com.infinbank.rest.exception.CourseEntityNotFoundException;
import com.infinbank.rest.mapper.CourseMapper;
import com.infinbank.rest.mapper.StudentMapper;
import com.infinbank.rest.mapper.TeacherMapper;
import com.infinbank.rest.model.Course;
import com.infinbank.rest.model.Student;
import com.infinbank.rest.model.Teacher;
import com.infinbank.rest.repository.CourseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseMapper courseMapper;
    @Mock
    private StudentMapper studentMapper;
    @Mock
    private TeacherMapper teacherMapper;
    @Mock
    private CourseRepository courseRepository;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void should_successfully_create_course() {
        // given
        CourseDto dto = CourseDto.builder()
                .courseName("Math")
                .build();

        CourseDto savedDto = CourseDto.builder()
                .id(1)
                .courseName("Math")
                .build();

        Course course = Course.builder()
                .courseName("Math")
                .build();

        Course savedCourse = Course.builder()
                .id(1)
                .courseName("Math")
                .build();

        // Mock the calls
        when(courseMapper.toEntity(dto))
                .thenReturn(course);
        when(courseRepository.save(course))
                .thenReturn(savedCourse);
        when(courseMapper.toDto(savedCourse))
                .thenReturn(savedDto);

        // when
        CourseDto savedCourseDto = courseService.save(dto);

        // Then
        assertNotNull(savedCourseDto);
        assertNotNull(savedCourseDto.getId());
        assertEquals(dto.getCourseName(), savedCourseDto.getCourseName());

        verify(courseMapper, times(1))
                .toEntity(dto);
        verify(courseRepository, times(1))
                .save(course);
        verify(courseMapper, times(1))
                .toDto(savedCourse);
    }

    @Test
    public void should_successfully_get_all_courses() {
        // given
        List<Course> courses = List.of(
                Course.builder()
                        .id(1)
                        .courseName("Math")
                        .build(),
                Course.builder()
                        .id(2)
                        .courseName("Biology")
                        .build()
        );

        List<CourseDto> courseDtos =
                List.of(
                        CourseDto.builder()
                                .id(1)
                                .courseName("Math")
                                .build(),
                        CourseDto.builder()
                                .id(2)
                                .courseName("Biology")
                                .build()
                );

        // mocking the calls
        when(courseRepository.findAll()).thenReturn(courses);
        when(courseMapper.toDto(courses.get(0))).thenReturn(courseDtos.get(0));
        when(courseMapper.toDto(courses.get(1))).thenReturn(courseDtos.get(1));

        // when
        List<CourseDto> resultCourses = courseService.findAll();

        // then
        assertThat(resultCourses).hasSize(2);

        verify(courseRepository, times(1)).findAll();
        verify(courseMapper, times(2)).toDto(any());
    }

    @Test
    public void should_successfully_get_course_by_id() {
        final int COURSE_ID = 1;
        // given
        Course course = Course.builder()
                .id(COURSE_ID)
                .courseName("Math")
                .build();

        CourseDto courseDto = CourseDto.builder()
                .id(COURSE_ID)
                .courseName("Math")
                .build();

        // mocking the calls
        when(courseRepository.findById(COURSE_ID))
                .thenReturn(Optional.of(course));
        when(courseMapper.toDto(course))
                .thenReturn(courseDto);

        // when
        CourseDto resultCourseDto = courseService.findById(COURSE_ID);

        // then
        assertNotNull(resultCourseDto);
        assertEquals(courseDto.getId(), resultCourseDto.getId());
        assertEquals(courseDto, resultCourseDto);

        verify(courseRepository, times(1))
                .findById(COURSE_ID);
        verify(courseMapper, times(1))
                .toDto(course);
    }

    @Test
    public void should_throw_exception_when_course_not_found() {
        final int COURSE_ID = 1;
        // given & mocking call
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CourseEntityNotFoundException.class, () -> courseService.findById(COURSE_ID));

        // verifying
        verify(courseRepository, times(1))
                .findById(COURSE_ID);
        verify(courseMapper, times(0))
                .toDto(any());
    }

    @Test
    public void should_successfully_update_course() {
        // given
        final int COURSE_ID = 1;
        CourseDto updatedCourseDto = CourseDto.builder()
                .id(COURSE_ID)
                .courseName("Math")
                .build();
        Course existingCourse = Course.builder()
                .id(COURSE_ID)
                .courseName("Science")
                .build();
        Course updatedCourse = Course.builder()
                .id(COURSE_ID)
                .courseName("Math")
                .build();

        // mocking calls
        when(courseRepository.findById(COURSE_ID))
                .thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(existingCourse))
                .thenReturn(updatedCourse);
        when(courseMapper.toDto(updatedCourse))
                .thenReturn(updatedCourseDto);

        // when
        CourseDto resultCourseDto = courseService.update(COURSE_ID, updatedCourseDto);

        // then
        assertNotNull(resultCourseDto);
        assertEquals(updatedCourseDto.getId(), resultCourseDto.getId());
        assertEquals(updatedCourseDto.getCourseName(), resultCourseDto.getCourseName());

        verify(courseRepository, times(1))
                .findById(any());
        verify(courseRepository, times(1))
                .save(existingCourse);
        verify(courseMapper, times(1))
                .toDto(updatedCourse);

    }

    @Test
    public void should_find_students_by_course_id() {
        final int COURSE_ID = 1;
        // given
        Student student1 = Student.builder()
                .firstName("Will1")
                .lastName("Salas1")
                .middleName("Sam1")
                .age(22)
                .build();
        Student student2 = Student.builder()
                .firstName("Will2")
                .lastName("Salas2")
                .middleName("Sam2")
                .age(23)
                .build();

        Course course = Course.builder()
                .id(COURSE_ID)
                .courseName("Math")
                .students(
                        List.of(student1, student2)
                ).build();

        StudentDto studentDto1 = StudentDto.builder()
                .firstName("Will1")
                .lastName("Salas1")
                .middleName("Sam1")
                .age(22)
                .build();
        StudentDto studentDto2 = StudentDto.builder()
                .firstName("Will2")
                .lastName("Salas2")
                .middleName("Sam2")
                .age(23)
                .build();

        // mocking calls
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
        when(studentMapper.toDto(student1)).thenReturn(studentDto1);
        when(studentMapper.toDto(student2)).thenReturn(studentDto2);

        // when
        List<StudentDto> resultStudents = courseService.findStudentsByCourse(COURSE_ID);

        // then
        assertNotNull(resultStudents);
        assertEquals(2, resultStudents.size());
        assertTrue(resultStudents.contains(studentDto1));
        assertTrue(resultStudents.contains(studentDto2));

        // verify
        verify(courseRepository, times(1)).findById(COURSE_ID);
        verify(studentMapper, times(1)).toDto(student1);
        verify(studentMapper, times(1)).toDto(student2);
    }

    @Test
    public void should_find_teachers_by_course_id() {
        final int COURSE_ID = 1;
        // given
        Teacher teacher1 = Teacher.builder()
                .firstName("Will1")
                .lastName("Salas1")
                .middleName("Sam1")
                .age(22)
                .build();
        Teacher teacher2 = Teacher.builder()
                .firstName("Will2")
                .lastName("Salas2")
                .middleName("Sam2")
                .age(23)
                .build();

        Course course = Course.builder()
                .id(COURSE_ID)
                .courseName("Math")
                .teachers(
                        List.of(teacher1, teacher2)
                ).build();

        TeacherDto teacherDto1 = TeacherDto.builder()
                .firstName("Will1")
                .lastName("Salas1")
                .middleName("Sam1")
                .age(22)
                .build();
        TeacherDto teacherDto2 = TeacherDto.builder()
                .firstName("Will2")
                .lastName("Salas2")
                .middleName("Sam2")
                .age(23)
                .build();

        // mocking calls
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
        when(teacherMapper.toDto(teacher1)).thenReturn(teacherDto1);
        when(teacherMapper.toDto(teacher2)).thenReturn(teacherDto2);

        // when
        List<TeacherDto> resultTeachers = courseService.findTeachersByCourse(COURSE_ID);

        // then
        assertNotNull(resultTeachers);
        assertEquals(2, resultTeachers.size());
        assertTrue(resultTeachers.contains(teacherDto1));
        assertTrue(resultTeachers.contains(teacherDto2));

        // verify
        verify(courseRepository, times(1)).findById(COURSE_ID);
        verify(teacherMapper, times(1)).toDto(teacher1);
        verify(teacherMapper, times(1)).toDto(teacher2);
    }
}
