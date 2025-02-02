package com.nguyenthanhdat.blog.controllers;

import com.nguyenthanhdat.blog.domain.dtos.post.PostListDto;
import com.nguyenthanhdat.blog.domain.entities.Post;
import com.nguyenthanhdat.blog.mappers.PostMapper;
import com.nguyenthanhdat.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        return ResponseEntity.ok(postService.createPost(post));
    }
}
