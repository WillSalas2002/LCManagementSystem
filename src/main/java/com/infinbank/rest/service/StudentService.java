package com.infinbank.rest.service;

import com.infinbank.rest.dto.CourseDto;
import com.infinbank.rest.dto.StudentDto;
import com.infinbank.rest.dto.TeacherDto;
import com.infinbank.rest.exception.CourseEntityNotFoundException;
import com.infinbank.rest.exception.StudentEntityNotFoundException;
import com.infinbank.rest.exception.TeacherEntityNotFoundException;
import com.infinbank.rest.mapper.CourseMapper;
import com.infinbank.rest.mapper.StudentMapper;
import com.infinbank.rest.mapper.TeacherMapper;
import com.infinbank.rest.model.Course;
import com.infinbank.rest.model.Student;
import com.infinbank.rest.model.Teacher;
import com.infinbank.rest.repository.CourseRepository;
import com.infinbank.rest.repository.StudentRepository;
import com.infinbank.rest.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService implements CrudService<StudentDto, Integer> {

    private final CourseMapper courseMapper;

    private final StudentMapper studentMapper;

    private final TeacherMapper teacherMapper;

    private final CourseRepository courseRepository;

    private final StudentRepository studentRepository;

    private final TeacherRepository teacherRepository;

    @Transactional(readOnly = true)
    public StudentDto findById(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentEntityNotFoundException(studentId));
        return studentMapper.toDto(student);
    }

    @Transactional(readOnly = true)
    public List<StudentDto> findAll() {
        List<Student> all = studentRepository.findAll();
        return all.stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentDto save(StudentDto studentDto) {
        Student student = studentMapper.toEntity(studentDto);
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDto(savedStudent);
    }

    @Transactional
    public StudentDto update(Integer studentId, StudentDto updatedStudentDto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentEntityNotFoundException(studentId));
        updateStudent(updatedStudentDto, student);
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDto(savedStudent);
    }

    @Transactional
    public void deleteById(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentEntityNotFoundException(studentId));
        studentRepository.delete(student);
    }

    @Transactional(readOnly = true)
    public List<CourseDto> findCoursesByStudentId(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentEntityNotFoundException(studentId));
        return student.getCourses().stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean assignCourseToStudent(Integer courseId, Integer studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseEntityNotFoundException(courseId));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentEntityNotFoundException(studentId));

        List<Course> courses = student.getCourses();
        if (courses.contains(course)) {
            return false;
        }
        courses.add(course);
        course.getStudents().add(student);
        studentRepository.save(student);
        return true;
    }

    @Transactional(readOnly = true)
    public List<TeacherDto> findTeachersByStudentId(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentEntityNotFoundException(studentId));
        List<Teacher> teachers = student.getTeachers();

        return teachers.stream()
                .map(teacherMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean assignTeacherToStudent(Integer teacherId, Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentEntityNotFoundException(studentId));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherEntityNotFoundException(teacherId));

        List<Teacher> teachers = student.getTeachers();
        if (teachers.contains(teacher)) {
            return false;
        }
        teachers.add(teacher);
        teacher.getStudents().add(student);
        studentRepository.save(student);
        return true;
    }

    @Transactional
    public boolean removeTeacherFromStudent(Integer teacherId, Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentEntityNotFoundException(studentId));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherEntityNotFoundException(teacherId));

        List<Teacher> teachers = student.getTeachers();
        if (!teachers.contains(teacher)) {
            return false;
        }
        teachers.remove(teacher);
        studentRepository.save(student);
        return true;
    }

    @Transactional
    public boolean removeCourseFromStudent(Integer courseId, Integer studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseEntityNotFoundException(courseId));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentEntityNotFoundException(studentId));

        List<Course> courses = student.getCourses();
        if (!courses.contains(course)) {
            return false;
        }
        courses.remove(course);
        studentRepository.save(student);
        return true;
    }

    private static void updateStudent(StudentDto updatedStudentDto, Student student) {
        student.setFirstName(updatedStudentDto.getFirstName());
        student.setLastName(updatedStudentDto.getLastName());
        student.setMiddleName(updatedStudentDto.getMiddleName());
        student.setAge(updatedStudentDto.getAge());
    }
}
