package com.nguyenthanhdat.blog.mappers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.DashboardCategoryDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostListDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardUpdatePostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardTagDto;
import com.nguyenthanhdat.blog.domain.entities.Post;
import com.nguyenthanhdat.blog.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
@Component
public interface DashboardPostMapper {
    default DashboardPostDto toDto(Post post) {
        if (post == null) return null;

        return DashboardPostDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .status(post.getStatus())
                .readingTime(post.getReadingTime())
                .slug(post.getSlug())
                .category(toCategoryDto(post))
                .tags(toTagDtoSet(post.getTags()))
                .thumbnailUrl(post.getThumbnailUrl())
                .contentImages(post.getContentImages())
                .build();
    }

    default DashboardPostListDto toPostListDto(Post post) {
        if (post == null) return null;

        return DashboardPostListDto.builder()
                .title(post.getTitle())
                .status(post.getStatus())
                .slug(post.getSlug())
                .category_name(post.getCategory().getName())
                .tags_name(post.getTags().stream()
                        .map(Tag::getName)
                        .collect(Collectors.joining(", ")))
                .build();
    }

    default DashboardUpdatePostDto toUpdatePostDto(Post post) {
        if (post == null) return null;

        return DashboardUpdatePostDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .status(post.getStatus())
                .readingTime(post.getReadingTime())
                .slug(post.getSlug())
                .category_id(post.getCategory().getId())
                .tag_ids(post.getTags().stream()
                        .map(Tag::getId)
                        .collect(Collectors.toSet()))
                .thumbnailUrl(post.getThumbnailUrl())
                .contentImages(post.getContentImages())
                .build();
    }

    private DashboardCategoryDto toCategoryDto(Post post) {
        if (post.getCategory() == null) return null;

        return DashboardCategoryDto.builder()
                .id(post.getCategory().getId())
                .name(post.getCategory().getName())
                .build();
    }

    private Set<DashboardTagDto> toTagDtoSet(Set<Tag> tags) {
        if (tags == null) return Set.of();

        return tags.stream()
                .map(tag -> DashboardTagDto.builder()
                        .id(tag.getId())
                        .name(tag.getName())
                        .build())
                .collect(Collectors.toSet());
    }
}
