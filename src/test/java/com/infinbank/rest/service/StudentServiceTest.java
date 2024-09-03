package com.infinbank.rest.service;

import com.infinbank.rest.dto.StudentDto;
import com.infinbank.rest.exception.StudentEntityNotFoundException;
import com.infinbank.rest.mapper.CourseMapper;
import com.infinbank.rest.mapper.StudentMapper;
import com.infinbank.rest.mapper.TeacherMapper;
import com.infinbank.rest.model.Student;
import com.infinbank.rest.repository.CourseRepository;
import com.infinbank.rest.repository.StudentRepository;
import com.infinbank.rest.repository.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private CourseMapper courseMapper;
    @Mock
    private StudentMapper studentMapper;
    @Mock
    private TeacherMapper teacherMapper;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private StudentRepository studentRepository;
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
    public void should_successfully_save_student() {
        // Given
        final int STUDENT_ID = 1;
        StudentDto studentDto = StudentDto.builder()
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();
        Student student = Student.builder()
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();
        StudentDto savedStudentDto = StudentDto.builder()
                .id(STUDENT_ID)
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();
        Student savedStudent = Student.builder()
                .id(STUDENT_ID)
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();

        // Mock the calls
        when(studentMapper.toEntity(studentDto)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(savedStudent);
        when(studentMapper.toDto(savedStudent)).thenReturn(savedStudentDto);

        // When
        StudentDto resultStudent = studentService.save(studentDto);

        // Then
        assertNotNull(resultStudent);
        assertEquals(savedStudentDto.getId(), resultStudent.getId());
        assertEquals(savedStudentDto.getFirstName(), resultStudent.getFirstName());
        assertEquals(savedStudentDto.getLastName(), resultStudent.getLastName());
        assertEquals(savedStudentDto.getMiddleName(), resultStudent.getMiddleName());
        assertEquals(savedStudentDto.getAge(), resultStudent.getAge());

        // Verify
        verify(studentMapper, times(1)).toEntity(studentDto);
        verify(studentMapper, times(1)).toDto(savedStudent);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    public void should_successfully_get_all_students() {
        // Given
        final int STUDENT_ID_1 = 1;
        final int STUDENT_ID_2 = 2;
        StudentDto studentDto1 = StudentDto.builder()
                .id(STUDENT_ID_1)
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();
        StudentDto studentDto2 = StudentDto.builder()
                .id(STUDENT_ID_2)
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();
        Student student1 = Student.builder()
                .id(STUDENT_ID_1)
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();
        Student student2 = Student.builder()
                .id(STUDENT_ID_2)
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();

        // Mock the calls
        when(studentRepository.findAll()).thenReturn(List.of(student1, student2));
        when(studentMapper.toDto(student1)).thenReturn(studentDto1);
        when(studentMapper.toDto(student2)).thenReturn(studentDto2);

        // When
        List<StudentDto> actualAll = studentService.findAll();

        // Then
        assertNotNull(actualAll);
        assertEquals(2, actualAll.size());
        assertEquals(List.of(studentDto1, studentDto2), actualAll);

        // Verify
        verify(studentRepository, times(1)).findAll();
        verify(studentMapper, times(1)).toDto(student1);
        verify(studentMapper, times(1)).toDto(student2);
    }

    @Test
    public void should_successfully_get_student_by_id() {
        // Given
        final int STUDENT_ID = 1;
        Student student = Student.builder()
                .id(STUDENT_ID)
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();
        StudentDto expectedStudentDto = StudentDto.builder()
                .id(STUDENT_ID)
                .firstName("Will")
                .lastName("Salas")
                .middleName("Sam")
                .age(22)
                .build();

        // Mock the calls
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(studentMapper.toDto(student)).thenReturn(expectedStudentDto);

        // When
        StudentDto actualStudentDto = studentService.findById(STUDENT_ID);

        // Then
        assertNotNull(actualStudentDto);
        assertEquals(expectedStudentDto.getId(), actualStudentDto.getId());
        assertEquals(expectedStudentDto.getFirstName(), actualStudentDto.getFirstName());
        assertEquals(expectedStudentDto.getLastName(), actualStudentDto.getLastName());
        assertEquals(expectedStudentDto.getMiddleName(), actualStudentDto.getMiddleName());
        assertEquals(expectedStudentDto.getAge(), actualStudentDto.getAge());

        // Verify
        verify(studentRepository, times(1)).findById(STUDENT_ID);
        verify(studentMapper, times(1)).toDto(student);

    }

    @Test
    public void should_throw_exception_when_student_not_found() {
        final int STUDENT_ID = 1;

        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(StudentEntityNotFoundException.class, () -> studentService.findById(STUDENT_ID));

        // Verify
        verify(studentRepository, times(1)).findById(STUDENT_ID);
        verify(studentMapper, never()).toDto(any());
    }

    @Test
    public void should_successfully_update_student() {
        final int STUDENT_ID = 1;
        StudentDto expectedStudentDto = StudentDto.builder()
                .id(STUDENT_ID)
                .firstName("Will new")
                .lastName("Salas new")
                .middleName("Sam new")
                .age(23)
                .build();
        Student oldStudent = Student.builder()
                .id(STUDENT_ID)
                .firstName("Will old")
                .lastName("Salas old")
                .middleName("Sam old")
                .age(22)
                .build();
        Student updatedStudent = Student.builder()
                .id(STUDENT_ID)
                .firstName("Will new")
                .lastName("Salas new")
                .middleName("Sam new")
                .age(23)
                .build();

        // Mock the calls
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(oldStudent));
        when(studentRepository.save(oldStudent)).thenReturn(updatedStudent);
        when(studentMapper.toDto(updatedStudent)).thenReturn(expectedStudentDto);

        // When
        StudentDto actualStudentDto = studentService.update(STUDENT_ID, expectedStudentDto);

        // Then
        assertNotNull(actualStudentDto);
        assertEquals(expectedStudentDto.getId(), actualStudentDto.getId());
        assertEquals(expectedStudentDto.getFirstName(), actualStudentDto.getFirstName());
        assertEquals(expectedStudentDto.getLastName(), actualStudentDto.getLastName());
        assertEquals(expectedStudentDto.getMiddleName(), actualStudentDto.getMiddleName());
        assertEquals(expectedStudentDto.getAge(), actualStudentDto.getAge());

        // Verify
        verify(studentRepository, times(1)).findById(STUDENT_ID);
        verify(studentRepository, times(1)).save(oldStudent);
        verify(studentMapper, times(1)).toDto(updatedStudent);
    }
}




















