package com.nguyenthanhdat.blog.domain.dtos.dashboard.post;

import com.nguyenthanhdat.blog.domain.enums.PostStatus;
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
public class DashboardUpdatePostDto {
    private String title;
    private String content;
    private PostStatus status;
    private Integer readingTime;
    private String slug;
    private UUID category_id;
    private Set<UUID> tag_ids;
    private String thumbnailUrl;
    private Set<String> contentImages;
}
