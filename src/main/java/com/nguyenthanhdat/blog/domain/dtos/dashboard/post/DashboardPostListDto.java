package com.nguyenthanhdat.blog.domain.dtos.dashboard.post;

import com.nguyenthanhdat.blog.domain.enums.PostStatus;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardPostListDto {
    private UUID id;
    private String title;
    private PostStatus status;
    private String slug;
    private String category_name;
    private String tags_name;
}

