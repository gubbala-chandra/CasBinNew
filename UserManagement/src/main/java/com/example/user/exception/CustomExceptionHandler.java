package com.example.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static com.example.user.util.Utils.formatSafe;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(RoleAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> roleAlreadyExists(HttpServletRequest request, RoleAlreadyExistsException ex, HandlerMethod method) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(generateErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(),method, request));
    }

    @ExceptionHandler(RoleNotPresetException.class)
    public ResponseEntity<ErrorResponse> roleNotPresent(HttpServletRequest request,RoleNotPresetException ex, HandlerMethod method) {
        ErrorResponse response = ErrorResponse.builder()
                .errorCode(HttpStatus.FORBIDDEN.value())
                .errorMessage(ex.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(generateErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(),method, request));
    }


    private ErrorResponse generateErrorResponse(Integer status, String message, HandlerMethod method, HttpServletRequest request) {
       return ErrorResponse.builder()
               .method(formatSafe("%s/%s", request.getMethod(),method.getMethod().getName()))
               .errorCode(status)
               .errorMessage(message)
               .errorTime(LocalDateTime.now())
               .build();
    }
}
