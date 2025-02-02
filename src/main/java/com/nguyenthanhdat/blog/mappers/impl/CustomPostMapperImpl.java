package com.nguyenthanhdat.blog.mappers.impl;

import com.nguyenthanhdat.blog.domain.dtos.category.CategoryDto;
import com.nguyenthanhdat.blog.domain.dtos.post.PostListDto;
import com.nguyenthanhdat.blog.domain.dtos.tag.TagDto;
import com.nguyenthanhdat.blog.domain.entities.Post;
import com.nguyenthanhdat.blog.mappers.PostMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Primary
@Component
public class CustomPostMapperImpl implements PostMapper {
    @Override
    public PostListDto toDto(Post post) {
        return PostListDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .status(post.getStatus())
                .readingTime(post.getReadingTime())
                .slug(post.getSlug())
                .category(new CategoryDto(post.getCategory().getId(), post.getCategory().getName()))
                .tags(post.getTags().stream()
                        .map(tag -> new TagDto(tag.getId(), tag.getName()))
                        .collect(Collectors.toSet()))
                .build();
    }
}
