package com.nguyenthanhdat.blog.domain.dtos.dashboard.post;

import com.nguyenthanhdat.blog.domain.enums.PostStatus;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.DashboardCategoryDto;
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
public class DashboardPostDto {
    private String title;
    private String content;
    private PostStatus status;
    private Integer readingTime;
    private String slug;
    private DashboardCategoryDto category;
    private Set<TagDto> tags;
    private String thumbnailUrl;
    private Set<String> contentImages;
}
