package com.nguyenthanhdat.blog.domain.dtos.dashboard.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardCreateCategoryDto {
    @NotBlank(message = "Category name is required")
    @Size(min = 3, max = 100, message = "Category name must be between 3 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Category name must contain only letters, numbers and spaces")
    private String name;

}
