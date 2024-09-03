package com.infinbank.rest.exception;

public abstract class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityTitle, Integer id) {
        super(entityTitle + " with id " + id + " not found");
    }
}
