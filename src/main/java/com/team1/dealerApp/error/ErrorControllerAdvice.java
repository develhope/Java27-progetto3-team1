package com.team1.dealerApp.error;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@SuppressWarnings("unused")
@RestControllerAdvice
public class ErrorControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDTO> handleBadRequestException (BadRequestException e){
        ErrorDTO errorDTO = ErrorDTO.builder().errorMessage(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(errorDTO);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDTO> handleNoSuchElementException (NoSuchElementException e){
        ErrorDTO errorDTO = ErrorDTO.builder().errorMessage(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(errorDTO);
    }
}
