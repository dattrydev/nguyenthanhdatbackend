package com.nguyenthanhdat.blog.domain.dtos.dashboard.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryListDto {
    private UUID id;
    private String name;
    private int postCount;
}
