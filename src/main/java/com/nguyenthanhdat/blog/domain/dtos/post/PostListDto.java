package com.nguyenthanhdat.blog.domain.dtos.post;

import com.nguyenthanhdat.blog.domain.PostStatus;
import com.nguyenthanhdat.blog.domain.dtos.category.CategoryDto;
import com.nguyenthanhdat.blog.domain.dtos.tag.TagDto;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostListDto {
    private String title;
    private String content;
    private PostStatus status;
    private Integer readingTime;
    private String slug;
    private CategoryDto category;
    private Set<TagDto> tags;
    private String thumbnailUrl;
}

