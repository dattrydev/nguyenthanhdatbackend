package com.nguyenthanhdat.blog.domain.dtos.dashboard.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardCategoryDto {
    private UUID id;
    private String name;
    private String slug;
}
