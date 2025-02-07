package com.nguyenthanhdat.blog.controllers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.auth.LoginResponseDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.auth.LoginRequestDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.auth.ValidateResponseDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.user.UserDto;
import com.nguyenthanhdat.blog.domain.entities.User;
import com.nguyenthanhdat.blog.services.AuthenticationService;
import com.nguyenthanhdat.blog.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/dashboard/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            User user = userService.findUserByEmail(loginRequestDto.getEmail());
            if (user == null) {
                log.warn("User not found: {}", loginRequestDto.getEmail());
                throw new BadCredentialsException("User not found");
            }

            var userDetails = authenticationService.authenticate(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            String tokenValue = authenticationService.generateToken(userDetails);
            LoginResponseDto loginResponseDto = authenticationService.generateLoginResponse(user, tokenValue);

            return ResponseEntity.ok(loginResponseDto);

        } catch (BadCredentialsException e) {
            log.error("Invalid credentials for email: {}", loginRequestDto.getEmail());
            throw new BadCredentialsException("Invalid credentials");
        } catch (Exception e) {
            log.error("Unexpected error during login", e);
            throw new RuntimeException("Unexpected error");
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
