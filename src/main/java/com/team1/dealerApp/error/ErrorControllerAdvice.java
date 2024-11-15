package com.team1.dealerApp.error;

import com.paypal.base.exception.PayPalException;
import com.paypal.core.rest.PayPalRESTException;
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

    @ExceptionHandler( PayPalException.class)
    public ResponseEntity<ErrorDTO> handlePayPalRestException ( PayPalRESTException e ){
        ErrorDTO errorDTO = ErrorDTO.builder().errorMessage(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(errorDTO);
    }

    @ExceptionHandler( NoSuchFieldException.class)
    public ResponseEntity<ErrorDTO> handleNoSuchFieldException ( NoSuchFieldException e){
        ErrorDTO errorDTO = ErrorDTO.builder().errorMessage(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(errorDTO);
    }

    @ExceptionHandler( IllegalAccessError.class)
    public ResponseEntity<ErrorDTO> handleIllegalAccessException ( IllegalAccessException e ){
        ErrorDTO errorDTO = ErrorDTO.builder().errorMessage(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(errorDTO);
    }

    @ExceptionHandler( NullPointerException.class )
    public ResponseEntity<ErrorDTO> handleNullPointerException(NullPointerException e){
        ErrorDTO errorDTO = ErrorDTO.builder().errorMessage(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(errorDTO);
    }
}
