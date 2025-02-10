package com.nguyenthanhdat.blog.services;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardCreatePostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostListPagingDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardUpdatePostDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostService {
        Optional<DashboardPostListPagingDto> getDashboardPostList(String title, List<String> status, Integer reading_time, List<String> category, List<String> tags, int page, int size, String sortBy, String sortDirection);

    Optional<DashboardPostDto> getPostBySlug(String slug);

    Optional<DashboardPostDto> createPost(DashboardCreatePostDto dashboardCreatePostDto);

    Optional<DashboardPostDto> updatePost(UUID id, DashboardUpdatePostDto postDto);

    void deletePost(UUID id);

    void deletePosts(List<UUID> ids);

    boolean isFieldExists(String field, String value);
}