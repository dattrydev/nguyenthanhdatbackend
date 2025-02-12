package com.nguyenthanhdat.blog.domain.dtos.blog.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogCategoryListDto {
    private String name;
    private String slug;
    private Integer postCount;
}
