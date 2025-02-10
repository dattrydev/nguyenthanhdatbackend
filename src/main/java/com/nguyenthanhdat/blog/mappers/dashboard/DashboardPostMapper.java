package com.nguyenthanhdat.blog.mappers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostListDto;
import com.nguyenthanhdat.blog.domain.entities.Post;
import com.nguyenthanhdat.blog.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
@Component
public interface DashboardPostMapper {
    DashboardPostDto toDto(Post post);

    default DashboardPostListDto toPostListDto(Post post) {
        if (post == null) return null;

        return DashboardPostListDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .status(post.getStatus())
                .reading_time(post.getReadingTime())
                .slug(post.getSlug())
                .category_name(post.getCategory().getName())
                .tags_name(post.getTags().stream()
                        .map(Tag::getName)
                        .collect(Collectors.joining(", ")))
                .build();
    }
}
