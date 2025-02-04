package com.nguyenthanhdat.blog.services;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.CreatePostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardUpdatePostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.PostDto;
import com.nguyenthanhdat.blog.domain.entities.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostService {
    public List<Post> getAllPosts();

    public Map<String, Object> getAllDashboardPosts(String title, String status, Integer readingTime, String category, int page, int size);

    public Optional<PostDto> getPostBySlug(String slug);

    public Post createPost(CreatePostDto createPostDto, MultipartFile thumbnail, List<MultipartFile> contentImages);

    public Post createPostWithoutImage(Post post);

    public DashboardUpdatePostDto updatePost(String slug, DashboardUpdatePostDto postDto, MultipartFile newThumbnail, List<MultipartFile> newContentImages, String oldThumbnail, List<String> oldContentImages);
}