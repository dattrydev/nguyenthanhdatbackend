package com.nguyenthanhdat.blog.domain.dtos.blog.post;

import com.nguyenthanhdat.blog.domain.dtos.blog.category.BlogCategoryDto;
import com.nguyenthanhdat.blog.domain.dtos.blog.tag.BlogTagDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPostDto {
    private String title;
    private String content;
    private BlogCategoryDto category;
    private Set<BlogTagDto> tags;
    private String slug;
    private LocalDateTime createdAt;
}
