package com.infinbank.rest.mapper;

import com.infinbank.rest.dto.StudentDto;
import com.infinbank.rest.model.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentDto toDto(Student student);

    Student toEntity(StudentDto studentDto);
}
