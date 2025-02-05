package com.nguyenthanhdat.blog.mappers.dashboard.impl;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.DashboardCategoryDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardCreatePostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostListDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardUpdatePostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.TagDto;
import com.nguyenthanhdat.blog.domain.entities.Post;
import com.nguyenthanhdat.blog.mappers.dashboard.DashboardPostMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Primary
@Component
public class CustomDashboardPostMapperImpl implements DashboardPostMapper {
    @Override
    public DashboardPostDto toDto(Post post) {
        return DashboardPostDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .status(post.getStatus())
                .readingTime(post.getReadingTime())
                .slug(post.getSlug())
                .category(DashboardCategoryDto.builder()
                        .id(post.getCategory().getId())
                        .name(post.getCategory().getName())
                        .build())
                .tags(post.getTags().stream()
                        .map(tag -> TagDto.builder()
                                .id(tag.getId())
                                .name(tag.getName())
                                .build())
                        .collect(Collectors.toSet()))
                .thumbnailUrl(post.getThumbnailUrl())
                .contentImages(post.getContentImages())
                .build();    }

    @Override
    public DashboardPostListDto toPostListDto(Post post) {
        return DashboardPostListDto.builder()
                .title(post.getTitle())
                .status(post.getStatus())
                .slug(post.getSlug())
                .category_name(post.getCategory().getName())
                .tags_name(post.getTags().stream()
                        .map(tag -> tag.getName())
                        .collect(Collectors.joining(", ")))
                .build();
    }

    @Override
    public DashboardUpdatePostDto toUpdatePostDto(Post post) {
        return DashboardUpdatePostDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .status(post.getStatus())
                .readingTime(post.getReadingTime())
                .slug(post.getSlug())
                .category_id(post.getCategory().getId())
                .tag_ids(post.getTags().stream()
                        .map(tag -> tag.getId())
                        .collect(Collectors.toSet()))
                .thumbnailUrl(post.getThumbnailUrl())
                .contentImages(post.getContentImages())
                .build();
    }
}
