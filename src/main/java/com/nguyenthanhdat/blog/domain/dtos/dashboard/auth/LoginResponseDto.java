package com.nguyenthanhdat.blog.domain.dtos.dashboard.auth;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {
    private String token;
    private long expiresIn;
    private UserDto user;
}
