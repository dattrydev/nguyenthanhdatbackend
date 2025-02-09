package com.nguyenthanhdat.blog.domain.dtos.dashboard.post;

import com.nguyenthanhdat.blog.domain.enums.PostStatus;
import jakarta.validation.constraints.*;
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

    @Size(min = 3, max = 255, message = "Post title must be between 3 and 255 characters")
    private String title;

    @Size(min = 10, max = 10000, message = "Post content must be between 10 and 10000 characters")
    private String content;

    private PostStatus status;

    private String slug;

    private UUID category_id;

    private Set<UUID> tags_id;

    private String thumbnailUrl;

    private Set<String> contentImages;
}
