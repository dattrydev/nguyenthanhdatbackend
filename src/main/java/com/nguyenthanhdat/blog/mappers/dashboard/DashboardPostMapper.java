package com.nguyenthanhdat.blog.mappers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostListDto;
import com.nguyenthanhdat.blog.domain.entities.Post;
import com.nguyenthanhdat.blog.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
@Component
public interface DashboardPostMapper {
    DashboardPostDto toDto(Post post);

    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "tagsName", source = "tags")
    DashboardPostListDto toPostListDto(Post post);

    default String mapTags(Set<Tag> tags) {
        return  tags.stream()
                .map(Tag::getName)
                .collect(Collectors.joining(", "));
    }
}
