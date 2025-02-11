package com.nguyenthanhdat.blog.domain.dtos.dashboard.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardCategoryListDto {
    private UUID id;
    private String name;
    private String slug;
}
