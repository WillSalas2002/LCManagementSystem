package com.infinbank.rest.exception;

public class TeacherEntityNotFoundException extends EntityNotFoundException {

    public TeacherEntityNotFoundException(Integer id) {
        super("Teacher", id);
    }
}
