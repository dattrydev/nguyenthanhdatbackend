package com.nguyenthanhdat.blog.exceptions.dashboard.handler;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.ApiErrorResponseDto;
import com.nguyenthanhdat.blog.exceptions.dashboard.post.PostAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@Order(1)
@ControllerAdvice
public class PostExceptionHandler {

    @ExceptionHandler(PostAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponseDto> handlePostAlreadyExistsException(PostAlreadyExistsException e) {
        log.error("Post already exists: {}", e.getMessage());

        ApiErrorResponseDto error = ApiErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
