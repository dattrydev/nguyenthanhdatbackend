package com.nguyenthanhdat.blog.services;

import com.nguyenthanhdat.blog.domain.dtos.post.CreatePostDto;
import com.nguyenthanhdat.blog.domain.dtos.post.PostDto;
import com.nguyenthanhdat.blog.domain.entities.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PostService {
    public List<Post> getAllPosts();
    public Optional<PostDto> getPostBySlug(String slug);
    public Post createPost(CreatePostDto createPostDto, MultipartFile thumbnail, List<MultipartFile> contentImages);
    public Post createPostWithoutImage(Post post);
}
