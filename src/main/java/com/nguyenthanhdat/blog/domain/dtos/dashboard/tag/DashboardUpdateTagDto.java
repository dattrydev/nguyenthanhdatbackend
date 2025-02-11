package com.nguyenthanhdat.blog.domain.dtos.dashboard.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardUpdateTagDto {
    @NotBlank(message = "Tag name is required")
    @Size(min = 2, max = 100, message = "Tag name must be between 2 and 100 characters")
    private String name;
}
