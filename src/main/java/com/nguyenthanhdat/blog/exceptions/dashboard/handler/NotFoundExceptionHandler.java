package com.nguyenthanhdat.blog.exceptions.dashboard.handler;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.ApiErrorResponseDto;
import com.nguyenthanhdat.blog.exceptions.dashboard.category.CategoryNotFoundException;
import com.nguyenthanhdat.blog.exceptions.dashboard.post.PostNotFoundException;
import com.nguyenthanhdat.blog.exceptions.dashboard.tag.TagNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Order(1)
@Slf4j
@ControllerAdvice
public class NotFoundExceptionHandler {

    @ExceptionHandler({PostNotFoundException.class, CategoryNotFoundException.class, TagNotFoundException.class})
    public ResponseEntity<ApiErrorResponseDto> handleNotFoundException(RuntimeException e) {
        log.error("Resource not found: {}", e.getMessage());

        ApiErrorResponseDto error = ApiErrorResponseDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
