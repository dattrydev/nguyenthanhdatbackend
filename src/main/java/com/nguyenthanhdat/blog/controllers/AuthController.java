package com.nguyenthanhdat.blog.controllers;

import com.nguyenthanhdat.blog.domain.dtos.auth.LoginResponseDto;
import com.nguyenthanhdat.blog.domain.dtos.LoginRequest;
import com.nguyenthanhdat.blog.domain.dtos.user.UserDto;
import com.nguyenthanhdat.blog.domain.entities.User;
import com.nguyenthanhdat.blog.services.AuthenticationService;
import com.nguyenthanhdat.blog.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.findUserByEmail(loginRequest.getEmail());
            if (user == null) {
                log.warn("User not found: {}", loginRequest.getEmail());
                throw new BadCredentialsException("Email or password is incorrect");
            }

            var userDetails = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            String tokenValue = authenticationService.generateToken(userDetails);

            UserDto userDto = UserDto.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .build();

            LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                    .token(tokenValue)
                    .expiresIn(86400)
                    .user(userDto)
                    .build();

            return ResponseEntity.ok(loginResponseDto);

        } catch (BadCredentialsException e) {
            log.error("Invalid credentials for email: {}", loginRequest.getEmail());
            throw new BadCredentialsException("Email or password is incorrect");
        } catch (Exception e) {
            log.error("Unexpected error during login", e);
            throw new IllegalStateException("An unexpected error occurred");
        }
    }
}
