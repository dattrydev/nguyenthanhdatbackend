package com.nguyenthanhdat.blog.domain.dtos.dashboard.post;

import com.nguyenthanhdat.blog.domain.enums.PostStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardCreatePostDto {

    @NotBlank(message = "Post title cannot be blank")
    @Size(min = 3, max = 255, message = "Post title must be between 3 and 255 characters")
    private String title;

    @NotNull(message = "Post description cannot be null")
    @Size(min = 3, max = 255, message = "Post description must be between 3 and 255 characters")
    private String description;

    @NotBlank(message = "Post content cannot be blank")
    @Size(min = 10, max = 10000, message = "Post content must be between 10 and 10000 characters")
    private String content;

    @NotNull(message = "Post status cannot be null")
    private PostStatus status;

    @NotNull(message = "Category ID cannot be null")
    private UUID categoryId;

    @NotEmpty(message = "At least one tag is required")
    private Set<UUID> tagsId;
}
