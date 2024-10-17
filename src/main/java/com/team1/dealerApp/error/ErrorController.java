package com.team1.dealerApp.error;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity< ErrorDTO > handleException (Exception e){
        ErrorDTO errorDTO = ErrorDTO.builder().errorMessage(e.getMessage()).build();
        return new ResponseEntity <>(errorDTO, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDTO> handleBadRequestException (BadRequestException e){
        ErrorDTO errorDTO = ErrorDTO.builder().errorMessage(e.getMessage()).build();
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDTO> handleNoSuchElementException (NoSuchElementException e){
        ErrorDTO errorDTO = ErrorDTO.builder().errorMessage(e.getMessage()).build();
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }
}
