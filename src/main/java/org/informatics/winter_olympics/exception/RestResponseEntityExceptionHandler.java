package org.informatics.winter_olympics.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;

@RestControllerAdvice(basePackages = "org.informatics.winter_olympics.controller.api")
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class, ValidationException.class, BusinessRuleException.class})
    public ResponseEntity<Object> handleConstraintViolationException(
            Exception exception, WebRequest request) {
        LinkedHashMap<String, String> responseBody = new LinkedHashMap<>();
        responseBody.put("type", "Constraint Violation Exception");
        responseBody.put("instance", request.getDescription(false));
        responseBody.put("message", exception.getMessage());
        responseBody.put("status code", String.valueOf(HttpStatus.BAD_REQUEST));
        return handleExceptionInternal(exception, responseBody,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers,
                                                               HttpStatusCode status, WebRequest request) {
        LinkedHashMap<String, String> responseBody = new LinkedHashMap<>();
        responseBody.put("type", "Method Argument is not valid!");
        responseBody.put("instance", request.getDescription(false));
        responseBody.put("message", exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .reduce("", (message1, message2) -> message1 + ", " + message2));
        responseBody.put("status code", String.valueOf(HttpStatus.BAD_REQUEST));
        return handleExceptionInternal(exception, responseBody,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Object> handleObjectNotFoundException(ObjectNotFoundException exception, WebRequest request) {
        String responseBody = "Object Not Found!";
        return handleExceptionInternal(exception, responseBody + " " + exception.getMessage(),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(
            Exception exception, WebRequest request) {
        String message = "Something went wrong!";
        return new ResponseEntity<Object>(
                message, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
