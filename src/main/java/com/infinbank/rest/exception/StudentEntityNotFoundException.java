package com.infinbank.rest.exception;

public class StudentEntityNotFoundException extends EntityNotFoundException {

    public StudentEntityNotFoundException(Integer id) {
        super("Student", id);
    }
}
