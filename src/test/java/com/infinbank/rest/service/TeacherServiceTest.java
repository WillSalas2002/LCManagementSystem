package com.infinbank.rest.service;

import com.infinbank.rest.dto.CourseDto;
import com.infinbank.rest.dto.TeacherDto;
import com.infinbank.rest.exception.TeacherEntityNotFoundException;
import com.infinbank.rest.mapper.CourseMapper;
import com.infinbank.rest.mapper.StudentMapper;
import com.infinbank.rest.mapper.TeacherMapper;
import com.infinbank.rest.model.Course;
import com.infinbank.rest.model.Teacher;
import com.infinbank.rest.repository.CourseRepository;
import com.infinbank.rest.repository.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TeacherServiceTest {
    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private CourseMapper courseMapper;
    @Mock
    private StudentMapper studentMapper;
    @Mock
    private TeacherMapper teacherMapper;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private TeacherRepository teacherRepository;
    private AutoCloseable mocks;
    @BeforeEach
    void setUp() {
        this.mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void should_successfully_save_teacher() {
        // Given
        final int STUDENT_ID = 1;
        TeacherDto teacherDto = TeacherDto.builder()
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();
        Teacher teacher = Teacher.builder()
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();
        TeacherDto savedTeacherDto = TeacherDto.builder()
                .id(STUDENT_ID)
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();
        Teacher savedTeacher = Teacher.builder()
                .id(STUDENT_ID)
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();

        // Mock the calls
        when(teacherMapper.toEntity(teacherDto)).thenReturn(teacher);
        when(teacherRepository.save(teacher)).thenReturn(savedTeacher);
        when(teacherMapper.toDto(savedTeacher)).thenReturn(savedTeacherDto);

        // When
        TeacherDto actualTeacher = teacherService.save(teacherDto);

        // Then
        assertNotNull(actualTeacher);
        assertEquals(savedTeacherDto.getId(), actualTeacher.getId());
        assertEquals(savedTeacherDto.getFirstName(), actualTeacher.getFirstName());
        assertEquals(savedTeacherDto.getLastName(), actualTeacher.getLastName());
        assertEquals(savedTeacherDto.getMiddleName(), actualTeacher.getMiddleName());
        assertEquals(savedTeacherDto.getAge(), actualTeacher.getAge());

        // Verify
        verify(teacherMapper, times(1)).toEntity(teacherDto);
        verify(teacherMapper, times(1)).toDto(savedTeacher);
        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    public void should_successfully_get_all_teachers() {
        // Given
        final int TEACHER_ID_1 = 1;
        final int TEACHER_ID_2 = 2;
        TeacherDto teacherDto1 = TeacherDto.builder()
                .id(TEACHER_ID_1)
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();
        TeacherDto teacherDto2 = TeacherDto.builder()
                .id(TEACHER_ID_2)
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();
        Teacher teacher1 = Teacher.builder()
                .id(TEACHER_ID_1)
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();
        Teacher teacher2 = Teacher.builder()
                .id(TEACHER_ID_2)
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();

        // Mock the calls
        when(teacherRepository.findAll()).thenReturn(List.of(teacher1, teacher2));
        when(teacherMapper.toDto(teacher1)).thenReturn(teacherDto1);
        when(teacherMapper.toDto(teacher2)).thenReturn(teacherDto2);

        // When
        List<TeacherDto> actualTeacherDtos = teacherService.findAll();

        // Then
        assertNotNull(actualTeacherDtos);
        assertEquals(2, actualTeacherDtos.size());
        assertEquals(List.of(teacherDto1, teacherDto2), actualTeacherDtos);

        // Verify
        verify(teacherRepository, times(1)).findAll();
        verify(teacherMapper, times(1)).toDto(teacher1);
        verify(teacherMapper, times(1)).toDto(teacher2);
    }

    @Test
    public void should_successfully_get_teacher_by_id() {
        // Given
        final int TEACHER_ID = 1;
        Teacher teacher = Teacher.builder()
                .id(TEACHER_ID)
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();
        TeacherDto expectedTeacherDto = TeacherDto.builder()
                .id(TEACHER_ID)
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();

        // Mock the calls
        when(teacherRepository.findById(TEACHER_ID)).thenReturn(Optional.of(teacher));
        when(teacherMapper.toDto(teacher)).thenReturn(expectedTeacherDto);

        // When
        TeacherDto actualTeacherDto = teacherService.findById(TEACHER_ID);

        // Then
        assertNotNull(actualTeacherDto);
        assertEquals(expectedTeacherDto.getId(), actualTeacherDto.getId());
        assertEquals(expectedTeacherDto.getFirstName(), actualTeacherDto.getFirstName());
        assertEquals(expectedTeacherDto.getLastName(), actualTeacherDto.getLastName());
        assertEquals(expectedTeacherDto.getMiddleName(), actualTeacherDto.getMiddleName());
        assertEquals(expectedTeacherDto.getAge(), actualTeacherDto.getAge());

        // Verify
        verify(teacherRepository, times(1)).findById(TEACHER_ID);
        verify(teacherMapper, times(1)).toDto(teacher);

    }

    @Test
    public void should_throw_exception_when_teacher_not_found() {
        final int TEACHER_ID = 1;

        when(teacherRepository.findById(TEACHER_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TeacherEntityNotFoundException.class, () -> teacherService.findById(TEACHER_ID));

        // Verify
        verify(teacherRepository, times(1)).findById(TEACHER_ID);
        verify(teacherMapper, never()).toDto(any());
    }

    @Test
    public void should_successfully_update_teacher() {
        final int TEACHER_ID = 1;
        TeacherDto expectedTeacherDto = TeacherDto.builder()
                .id(TEACHER_ID)
                .firstName("Will new")
                .lastName("Salas new")
                .middleName("Sam new")
                .age(23)
                .build();
        Teacher oldTeacher = Teacher.builder()
                .id(TEACHER_ID)
                .firstName("Will old")
                .lastName("Salas old")
                .middleName("Sam old")
                .age(22)
                .build();
        Teacher updatedTeacher = Teacher.builder()
                .id(TEACHER_ID)
                .firstName("Will new")
                .lastName("Salas new")
                .middleName("Sam new")
                .age(23)
                .build();

        // Mock the calls
        when(teacherRepository.findById(TEACHER_ID)).thenReturn(Optional.of(oldTeacher));
        when(teacherRepository.save(oldTeacher)).thenReturn(updatedTeacher);
        when(teacherMapper.toDto(updatedTeacher)).thenReturn(expectedTeacherDto);

        // When
        TeacherDto actualTeacherDto = teacherService.update(TEACHER_ID, expectedTeacherDto);

        // Then
        assertNotNull(actualTeacherDto);
        assertEquals(expectedTeacherDto.getId(), actualTeacherDto.getId());
        assertEquals(expectedTeacherDto.getFirstName(), actualTeacherDto.getFirstName());
        assertEquals(expectedTeacherDto.getLastName(), actualTeacherDto.getLastName());
        assertEquals(expectedTeacherDto.getMiddleName(), actualTeacherDto.getMiddleName());
        assertEquals(expectedTeacherDto.getAge(), actualTeacherDto.getAge());

        // Verify
        verify(teacherRepository, times(1)).findById(TEACHER_ID);
        verify(teacherRepository, times(1)).save(oldTeacher);
        verify(teacherMapper, times(1)).toDto(updatedTeacher);
    }

    @Test
    public void should_successfully_assign_course_to_teacher_if_course_null() {
        // Given
        final int COURSE_ID = 1;
        final int TEACHER_ID = 1;
        Course course = Course.builder()
                .id(COURSE_ID)
                .teachers(new ArrayList<>())
                .build();
        Teacher teacher = Teacher.builder()
                .id(TEACHER_ID)
                .course(null)
                .build();

        // Mock the calls
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
        when(teacherRepository.findById(TEACHER_ID)).thenReturn(Optional.of(teacher));

        // When
        boolean isAssigned = teacherService.assignCourseToTeacher(COURSE_ID, TEACHER_ID);

        // Then
        assertTrue(isAssigned);
        assertNotNull(teacher.getCourse());
        assertSame(course, teacher.getCourse());
        assertTrue(course.getTeachers().contains(teacher));
        assertEquals(1, course.getTeachers().size());

        // Verify
        verify(courseRepository, times(1)).findById(COURSE_ID);
        verify(teacherRepository, times(1)).findById(TEACHER_ID);
        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    public void should_not_allow_assigning_course_to_teacher_when_course_not_null() {
        // Given
        final int COURSE_ID = 1;
        final int TEACHER_ID = 1;
        Course course = Course.builder()
                .id(COURSE_ID)
                .teachers(new ArrayList<>())
                .build();
        Teacher teacher = Teacher.builder()
                .id(TEACHER_ID)
                .course(course)
                .build();

        // Mock the calls
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
        when(teacherRepository.findById(TEACHER_ID)).thenReturn(Optional.of(teacher));

        // When
        boolean isAssigned = teacherService.assignCourseToTeacher(COURSE_ID, TEACHER_ID);

        // Then
        assertFalse(isAssigned);
        assertNotNull(teacher.getCourse());

        // Verify
        verify(courseRepository, times(1)).findById(COURSE_ID);
        verify(teacherRepository, times(1)).findById(TEACHER_ID);
        verify(teacherRepository, never()).save(any());
    }
    
    @Test
    public void should_successfully_get_course_of_teacher() {
        // Given
        final int TEACHER_ID = 1;
        Course course = Course.builder()
                .id(1)
                .courseName("Math 101")
                .build();
        CourseDto courseDto = CourseDto.builder()
                .id(1)
                .courseName("Math 101")
                .build();

        // Mock the calls
        when(courseRepository.findByTeacherId(TEACHER_ID)).thenReturn(Optional.of(course));
        when(courseMapper.toDto(course)).thenReturn(courseDto);

        // When
        Optional<CourseDto> result = teacherService.findCourseOfTeacher(TEACHER_ID);

        // Then
        assertTrue(result.isPresent());
        assertEquals(courseDto, result.get());

        // Verify
        verify(courseRepository, times(1)).findByTeacherId(TEACHER_ID);
        verify(courseMapper, times(1)).toDto(course);
    }
}
