package com.nguyenthanhdat.blog.controllers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.auth.LoginResponseDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.auth.LoginRequestDto;
import com.nguyenthanhdat.blog.domain.entities.User;
import com.nguyenthanhdat.blog.services.AuthenticationService;
import com.nguyenthanhdat.blog.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
