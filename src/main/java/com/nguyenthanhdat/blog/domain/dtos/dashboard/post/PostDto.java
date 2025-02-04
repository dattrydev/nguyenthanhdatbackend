package com.nguyenthanhdat.blog.domain.dtos.dashboard.post;

import com.nguyenthanhdat.blog.domain.PostStatus;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.CategoryDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.TagDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private String title;
    private String content;
    private PostStatus status;
    private Integer readingTime;
    private String slug;
    private CategoryDto category;
    private Set<TagDto> tags;
    private String thumbnailUrl;
    private Set<String> contentImages;
}
