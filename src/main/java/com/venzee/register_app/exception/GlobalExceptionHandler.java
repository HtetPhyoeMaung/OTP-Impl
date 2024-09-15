package com.venzee.register_app.exception;

import com.venzee.register_app.dto.response.exception.CommonExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<CommonExceptionResponse> alreadyExists(AlreadyExistsException exception){
        CommonExceptionResponse response = new CommonExceptionResponse();
        response.setStatus(HttpStatus.ALREADY_REPORTED.value());
        response.setMessage(exception.getMessage());
        response.setTimeStamp(new Date());

        return new ResponseEntity<>(response,HttpStatus.ALREADY_REPORTED);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CommonExceptionResponse> alreadyExists(CommonExceptionResponse exception){
        CommonExceptionResponse response = new CommonExceptionResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(exception.getMessage());
        response.setTimeStamp(new Date());

        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }
}
