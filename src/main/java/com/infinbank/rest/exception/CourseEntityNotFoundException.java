package com.infinbank.rest.exception;

public class CourseEntityNotFoundException extends EntityNotFoundException {

    public CourseEntityNotFoundException(Integer id) {
        super("Course", id);
    }
}
