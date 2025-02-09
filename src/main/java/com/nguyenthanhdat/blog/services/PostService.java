package com.nguyenthanhdat.blog.services;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardCreatePostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostListPagingDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardUpdatePostDto;

import java.util.Optional;
import java.util.UUID;

public interface PostService {
    Optional<DashboardPostListPagingDto> getDashboardPostList(String title, String status, Integer readingTime, String category, int page, int size, String sortBy, String sortDirection);

    Optional<DashboardPostDto> getPostBySlug(String slug);

    Optional<DashboardPostDto> createPost(DashboardCreatePostDto dashboardCreatePostDto);

    Optional<DashboardPostDto> updatePost(UUID id, DashboardUpdatePostDto postDto);

    boolean isFieldExists(String field, String value);
}