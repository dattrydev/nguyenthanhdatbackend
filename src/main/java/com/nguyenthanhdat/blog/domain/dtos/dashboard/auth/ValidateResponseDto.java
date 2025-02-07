package com.nguyenthanhdat.blog.domain.dtos.dashboard.auth;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateResponseDto {
    private String message;
    private UserDto user;
}
