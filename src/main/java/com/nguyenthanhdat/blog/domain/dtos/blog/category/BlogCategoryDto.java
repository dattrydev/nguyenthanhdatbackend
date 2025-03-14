package com.nguyenthanhdat.blog.domain.dtos.blog.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogCategoryDto {
    private String name;
    private String slug;
}
