package com.nguyenthanhdat.blog.exceptions.handler;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.ApiErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@Order(1)
@ControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponseDto> handleBadCredentialsException(BadCredentialsException e) {
        log.error("Authentication error: ", e);
        ApiErrorResponseDto error = ApiErrorResponseDto.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Invalid username or password")
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponseDto> handleAccessDeniedException(AccessDeniedException e) {
        log.error("Access denied: ", e);
        ApiErrorResponseDto error = ApiErrorResponseDto.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message("You do not have permission to access this resource")
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponseDto> handleAuthenticationException(AuthenticationException e) {
        log.error("Authentication required: ", e);
        ApiErrorResponseDto error = ApiErrorResponseDto.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Authentication is required")
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}
