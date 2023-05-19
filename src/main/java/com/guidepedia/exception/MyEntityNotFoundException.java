package com.guidepedia.exception;

public class MyEntityNotFoundException extends RuntimeException {
    public MyEntityNotFoundException(Long id) {
        super("Entity is not found, id="+id);
    }

    public MyEntityNotFoundException(String name) {
        super("Entity is not found, parameter="+name);
    }
}