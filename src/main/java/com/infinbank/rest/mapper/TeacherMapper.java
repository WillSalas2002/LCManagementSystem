package com.infinbank.rest.mapper;

import com.infinbank.rest.dto.TeacherDto;
import com.infinbank.rest.model.Teacher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CourseMapper.class)
public interface TeacherMapper {

    TeacherDto toDto(Teacher teacher);

    Teacher toEntity(TeacherDto teacherDto);
}
