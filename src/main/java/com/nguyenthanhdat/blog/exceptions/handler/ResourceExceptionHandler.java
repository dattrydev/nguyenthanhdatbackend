package com.nguyenthanhdat.blog.exceptions.handler;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.ApiErrorResponseDto;
import com.nguyenthanhdat.blog.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@Order(1)
@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponseDto> handleNotFoundException(ResourceNotFoundException e) {
        log.error("Resource not found: {}", e.getMessage());

        ApiErrorResponseDto error = ApiErrorResponseDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponseDto> handleResourceAlreadyExistsException(ResourceAlreadyExistsException e) {
        log.error("Resource already exists: {}", e.getMessage());

        ApiErrorResponseDto error = ApiErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ApiErrorResponseDto> handleFileUploadException(FileUploadException e) {
        log.error("File upload error: ", e);

        ApiErrorResponseDto error = ApiErrorResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceCreationException.class)
    public ResponseEntity<ApiErrorResponseDto> handleResourceCreationException(ResourceCreationException e) {
        log.error("Resource creation error: ", e);

        ApiErrorResponseDto error = ApiErrorResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceUpdateException.class)
    public ResponseEntity<ApiErrorResponseDto> handleResourceUpdateException(ResourceUpdateException e) {
        log.error("Resource update error: ", e);

        ApiErrorResponseDto error = ApiErrorResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceDeleteException.class)
    public ResponseEntity<ApiErrorResponseDto> handleResourceDeleteException(ResourceDeleteException e) {
        log.error("Resource delete error: ", e);

        ApiErrorResponseDto error = ApiErrorResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
