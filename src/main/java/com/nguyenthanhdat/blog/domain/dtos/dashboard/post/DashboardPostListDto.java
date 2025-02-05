package com.nguyenthanhdat.blog.domain.dtos.dashboard.post;

import com.nguyenthanhdat.blog.domain.enums.PostStatus;
import lombok.*;

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

