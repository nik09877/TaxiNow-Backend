package com.taxinow.exception;

import com.taxinow.response.ErrorDetail;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetail>userExceptionHandler(UserException ue, WebRequest req){
     ErrorDetail errorDetail = new ErrorDetail(ue.getMessage(),req.getDescription(false), LocalDateTime.now());
     return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DriverException.class)
    public ResponseEntity<ErrorDetail>userExceptionHandler(DriverException de, WebRequest req){
        ErrorDetail errorDetail = new ErrorDetail(de.getMessage(),req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RideException.class)
    public ResponseEntity<ErrorDetail>userExceptionHandler(RideException re, WebRequest req){
        ErrorDetail errorDetail = new ErrorDetail(re.getMessage(),req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    //FOR VALIDATION EXCEPTION HANDLING of REQUEST MODELS
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetail>validationExceptionHandler(ConstraintViolationException ce){
        StringBuilder errorMsg = new StringBuilder();
        for(ConstraintViolation<?> violation : ce.getConstraintViolations()){
            errorMsg.append(violation.getMessage()).append("\n");
        }
        ErrorDetail errorDetail = new ErrorDetail(errorMsg.toString(),"Validation Error", LocalDateTime.now());
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetail>methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException me){
        ErrorDetail errorDetail = new ErrorDetail(me.getBindingResult().getFieldError().getDefaultMessage(),"Validation Error",LocalDateTime.now());
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail>userExceptionHandler(Exception e, WebRequest req){
        ErrorDetail errorDetail = new ErrorDetail(e.getMessage(),req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }
}
