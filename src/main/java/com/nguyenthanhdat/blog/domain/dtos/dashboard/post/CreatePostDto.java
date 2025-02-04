package com.nguyenthanhdat.blog.domain.dtos.dashboard.post;

import com.nguyenthanhdat.blog.domain.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostDto {
    private String title;
    private String content;
    private PostStatus status;
    private int readingTime;
    private UUID category_id;
    private Set<UUID> tag_ids;
    private String thumbnailUrl;
    private Set<MultipartFile> contentImages;
}
