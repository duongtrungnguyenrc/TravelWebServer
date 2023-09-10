package com.web.travel.exception;

import com.web.travel.payload.response.MessageResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class AuthExceptionHandler{
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public MessageResponse handleConstrainViolationException(ConstraintViolationException ex, WebRequest request) {
        return new MessageResponse(0,"Please enter the correct information");
    }
}
