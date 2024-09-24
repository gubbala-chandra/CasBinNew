package com.example.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GroupAlreadyExistsException extends RuntimeException{

    public GroupAlreadyExistsException(String message) {
        super(message);
    }
}
