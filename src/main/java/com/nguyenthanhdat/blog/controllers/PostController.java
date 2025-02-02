package com.nguyenthanhdat.blog.controllers;

import com.nguyenthanhdat.blog.domain.dtos.post.CreatePostDto;
import com.nguyenthanhdat.blog.domain.dtos.post.PostListDto;
import com.nguyenthanhdat.blog.domain.entities.Post;
import com.nguyenthanhdat.blog.mappers.PostMapper;
import com.nguyenthanhdat.blog.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;

    @GetMapping
    public List<PostListDto> getAllPosts() {
        return postService.getAllPosts()
                .stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Post> getPostBySlug(@PathVariable String slug) {
        return postService.getPostBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Post> createPost(
            @ModelAttribute  CreatePostDto createPostDto,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam(value = "contentImages", required = false) List<MultipartFile> contentImages
    ) {
        Post savedPost = postService.createPost(createPostDto, thumbnail, contentImages);
        return ResponseEntity.ok(savedPost);
    }
}
