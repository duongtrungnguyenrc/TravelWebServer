package com.web.travel.exception;

import com.web.travel.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> methodArgumentNotValidExceptionHandler(){
        return ResponseEntity.badRequest().body(
                new MessageResponse(0, "Invalid information")
        );
    }
}
