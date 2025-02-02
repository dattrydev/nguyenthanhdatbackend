package com.nguyenthanhdat.blog.domain.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private String title;
    private String content;
    private String thumbnailUrl;
    private String slug;
    private String category;
    private String tags;
    private String status;
    private Integer readingTime;
}
