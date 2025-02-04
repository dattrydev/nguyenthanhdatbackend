package com.nguyenthanhdat.blog.domain.dtos.dashboard.post;

import com.nguyenthanhdat.blog.domain.PostStatus;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.CategoryDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.TagDto;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardPostListDto {
    private String title;
    private PostStatus status;
    private String slug;
    private String category_name;
    private String tags_name;
}

