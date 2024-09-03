package com.infinbank.rest.service;

import com.infinbank.rest.dto.CourseDto;
import com.infinbank.rest.dto.StudentDto;
import com.infinbank.rest.dto.TeacherDto;
import com.infinbank.rest.exception.CourseEntityNotFoundException;
import com.infinbank.rest.exception.TeacherEntityNotFoundException;
import com.infinbank.rest.mapper.CourseMapper;
import com.infinbank.rest.mapper.StudentMapper;
import com.infinbank.rest.mapper.TeacherMapper;
import com.infinbank.rest.model.Course;
import com.infinbank.rest.model.Student;
import com.infinbank.rest.model.Teacher;
import com.infinbank.rest.repository.CourseRepository;
import com.infinbank.rest.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService implements CrudService<TeacherDto, Integer> {

    private final CourseMapper courseMapper;

    private final StudentMapper studentMapper;

    private final TeacherMapper teacherMapper;

    private final CourseRepository courseRepository;

    private final TeacherRepository teacherRepository;

    @Transactional(readOnly = true)
    public TeacherDto findById(Integer teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherEntityNotFoundException(teacherId));
        return teacherMapper.toDto(teacher);
    }

    @Transactional(readOnly = true)
    public List<TeacherDto> findAll() {
        List<Teacher> all = teacherRepository.findAll();
        return all.stream()
                .map(teacherMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TeacherDto save(TeacherDto teacherDto) {
        Teacher teacher = teacherMapper.toEntity(teacherDto);
        Teacher savedTeacher = teacherRepository.save(teacher);
        return teacherMapper.toDto(savedTeacher);
    }

    @Transactional
    public TeacherDto update(Integer teacherId, TeacherDto updatedTeacherDto) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherEntityNotFoundException(teacherId));
        updateTeacher(updatedTeacherDto, teacher);
        Teacher savedTeacher = teacherRepository.save(teacher);
        return teacherMapper.toDto(savedTeacher);
    }

    @Transactional
    public void deleteById(Integer teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherEntityNotFoundException(teacherId));
        teacherRepository.delete(teacher);
    }

    @Transactional
    public boolean assignCourseToTeacher(Integer courseId, Integer teacherId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseEntityNotFoundException(courseId));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherEntityNotFoundException(teacherId));

        if (teacher.getCourse() != null) {
            return false;
        }
        teacher.setCourse(course);
        course.getTeachers().add(teacher);
        teacherRepository.save(teacher);
        return true;
    }

    @Transactional(readOnly = true)
    public Optional<CourseDto> findCourseOfTeacher(Integer teacherId) {
        return courseRepository.findByTeacherId(teacherId)
                .map(courseMapper::toDto)
                .or(() -> {
                    throw new TeacherEntityNotFoundException(teacherId);
                });

    }

    @Transactional(readOnly = true)
    public List<StudentDto> findStudentsOfTeacher(Integer teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherEntityNotFoundException(teacherId));
        List<Student> students = teacher.getStudents();
        return students.stream().map(studentMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public boolean removeCourseFromTeacher(Integer courseId, Integer teacherId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseEntityNotFoundException(courseId));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherEntityNotFoundException(teacherId));

        if (teacher.getCourse() == null) {
            return false;
        }
        course.getTeachers().remove(teacher);
        teacher.setCourse(null);
        teacherRepository.save(teacher);
        return true;
    }

    private static void updateTeacher(TeacherDto updatedTeacherDto, Teacher teacher) {
        teacher.setFirstName(updatedTeacherDto.getFirstName());
        teacher.setLastName(updatedTeacherDto.getLastName());
        teacher.setMiddleName(updatedTeacherDto.getMiddleName());
        teacher.setAge(updatedTeacherDto.getAge());
    }
}
