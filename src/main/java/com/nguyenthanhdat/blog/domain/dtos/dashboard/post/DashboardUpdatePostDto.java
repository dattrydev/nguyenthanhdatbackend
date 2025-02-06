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

    @NotBlank(message = "Post title cannot be blank")
    @Size(min = 3, max = 255, message = "Post title must be between 3 and 255 characters")
    private String title;

    @NotBlank(message = "Post content cannot be blank")
    @Size(min = 10, max = 10000, message = "Post content must be between 10 and 10000 characters")
    private String content;

    @NotNull(message = "Post status cannot be null")
    private PostStatus status;

    @Min(value = 1, message = "Reading time must be at least 1 minute")
    @Max(value = 1000, message = "Reading time must be less than or equal to 1000 minutes")
    private Integer readingTime;

    private String slug;

    @NotNull(message = "Category ID cannot be null")
    private UUID category_id;

    @NotEmpty(message = "At least one tag is required")
    private Set<UUID> tag_ids;

    private String thumbnailUrl;

    @NotEmpty(message = "At least one content image is required")
    private Set<String> contentImages;
}
