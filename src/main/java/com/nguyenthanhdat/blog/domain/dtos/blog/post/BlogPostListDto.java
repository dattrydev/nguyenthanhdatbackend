package com.nguyenthanhdat.blog.domain.dtos.blog.post;

import com.nguyenthanhdat.blog.domain.dtos.blog.category.BlogCategoryDto;
import com.nguyenthanhdat.blog.domain.dtos.blog.tag.BlogTagDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPostListDto {
    private String title;
    private String slug;
    private String description;
    private BlogCategoryDto category;
    private Set<BlogTagDto> tags;
    private LocalDateTime createdAt;
}
