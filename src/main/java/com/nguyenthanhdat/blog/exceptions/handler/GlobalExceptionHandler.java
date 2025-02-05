package com.nguyenthanhdat.blog.exceptions.handler;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.ApiErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponseDto> handleInvalidMediaTypeException(HttpMediaTypeNotSupportedException e) {
        log.error("Invalid media type: ", e);
        ApiErrorResponseDto error = ApiErrorResponseDto.builder()
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .message("Unsupported media type")
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponseDto> handleGeneralException(Exception e) {
        log.error("Unexpected error: ", e);
        ApiErrorResponseDto error = ApiErrorResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred")
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
