package com.nguyenthanhdat.blog.exceptions.handler;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.ApiErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@Order(1)
@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponseDto> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Validation error: ", e);

        var errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new ApiErrorResponseDto.FieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        ApiErrorResponseDto error = ApiErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .errors(errors)
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
