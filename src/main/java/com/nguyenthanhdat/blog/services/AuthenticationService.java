package com.nguyenthanhdat.blog.services;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.auth.LoginResponseDto;
import com.nguyenthanhdat.blog.domain.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {
    UserDetails authenticate(String email, String password);
    String generateToken(UserDetails userDetails);
    LoginResponseDto generateLoginResponse(User user, String tokenValue);
    UserDetails validateToken(String token);
}
