package com.nguyenthanhdat.blog.domain.dtos.dashboard.post;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardTagDto;
import com.nguyenthanhdat.blog.domain.enums.PostStatus;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.DashboardCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardPostDto {
    private UUID id;
    private String title;
    private String content;
    private PostStatus status;
    private Integer reading_time;
    private String slug;
    private DashboardCategoryDto category;
    private Set<DashboardTagDto> tags;
}
