package com.nguyenthanhdat.blog.mappers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostListDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardUpdatePostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.PostDto;
import com.nguyenthanhdat.blog.domain.entities.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface DashboardPostMapper {
    PostDto toDto(Post post);
    DashboardPostListDto toDashboardPostListDto(Post post);
    DashboardUpdatePostDto toDashboardUpdatePostDto(Post post);
}
