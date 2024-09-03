package com.infinbank.rest.mapper;

import com.infinbank.rest.dto.CourseDto;
import com.infinbank.rest.model.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", disableSubMappingMethodsGeneration = true)
public interface CourseMapper {

    CourseDto toDto(Course course);

    Course toEntity(CourseDto courseDto);
}
