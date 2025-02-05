package com.nguyenthanhdat.blog.services;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardCreatePostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardUpdatePostDto;
import com.nguyenthanhdat.blog.domain.entities.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostService {
    public List<Post> getAllPosts();

    public Map<String, Object> getDashboardPostList(String title, String status, Integer readingTime, String category, int page, int size);

    public Optional<DashboardPostDto> getPostBySlug(String slug);

    public DashboardPostDto createPost(DashboardCreatePostDto dashboardCreatePostDto, MultipartFile thumbnail, List<MultipartFile> contentImages);

    public Post createPostWithoutImage(Post post);

    public DashboardUpdatePostDto updatePost(String slug, DashboardUpdatePostDto postDto, MultipartFile newThumbnail, List<MultipartFile> newContentImages, String oldThumbnail, List<String> oldContentImages);
}