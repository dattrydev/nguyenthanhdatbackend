package com.nguyenthanhdat.blog.domain.dtos.auth;

import com.nguyenthanhdat.blog.domain.dtos.user.UserDto;
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
