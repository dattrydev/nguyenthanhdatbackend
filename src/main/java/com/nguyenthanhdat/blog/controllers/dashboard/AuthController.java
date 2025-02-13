package com.nguyenthanhdat.blog.controllers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.auth.LoginResponseDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.auth.LoginRequestDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.auth.ValidateResponseDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.user.UserDto;
import com.nguyenthanhdat.blog.domain.entities.User;
import com.nguyenthanhdat.blog.services.AuthenticationService;
import com.nguyenthanhdat.blog.services.UserService;
import com.nguyenthanhdat.blog.services.RateLimitService;
import com.nguyenthanhdat.blog.exceptions.TooManyLoginAttemptsException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/dashboard/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final RateLimitService rateLimitService;

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        String userEmail = loginRequestDto.getEmail();

        try {
            rateLimitService.tryConsume(userEmail);
        } catch (TooManyLoginAttemptsException e) {
            throw new TooManyLoginAttemptsException("Too many login attempts. Please try again later.");
        }

        try {
            User user = userService.findUserByEmail(userEmail);
            if (user == null) {
                rateLimitService.recordFailedAttempt(userEmail);
                throw new BadCredentialsException("User or password is incorrect");
            }

            var userDetails = authenticationService.authenticate(userEmail, loginRequestDto.getPassword());
            String tokenValue = authenticationService.generateToken(userDetails);
            LoginResponseDto loginResponseDto = authenticationService.generateLoginResponse(user, tokenValue);

            rateLimitService.resetFailedAttempts(userEmail);
            return ResponseEntity.ok(loginResponseDto);

        } catch (BadCredentialsException e) {
            rateLimitService.recordFailedAttempt(userEmail);
            throw new BadCredentialsException("User or password is incorrect");
        } catch (Exception e) {
            throw new BadCredentialsException("User or password is incorrect");
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<ValidateResponseDto> validateToken(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(401).body(
                    ValidateResponseDto.builder()
                            .message("Token not provided")
                            .build()
            );
        }

        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            UserDetails userDetails = authenticationService.validateToken(token);
            if (userDetails == null) {
                return ResponseEntity.status(401).body(
                        ValidateResponseDto.builder()
                                .message("Invalid token")
                                .build()
                );
            }

            return ResponseEntity.ok(
                    ValidateResponseDto.builder()
                            .message("Token is valid")
                            .user(
                                    UserDto.builder()
                                            .email(userDetails.getUsername())
                                            .build())
                            .build()
            );
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(
                    ValidateResponseDto.builder()
                            .message("Token expired")
                            .build()
            );
        } catch (MalformedJwtException | SignatureException e) {
            return ResponseEntity.status(401).body(
                    ValidateResponseDto.builder()
                            .message("Invalid token")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(401).body(
                    ValidateResponseDto.builder()
                            .message("Unexpected error")
                            .build()
            );
        }
    }
}
