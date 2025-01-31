package com.nguyenthanhdat.blog.controllers;

import com.nguyenthanhdat.blog.domain.entities.Post;
import com.nguyenthanhdat.blog.services.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostServiceImpl postServiceImpl;

    @GetMapping
    public List<Post> getAllPosts() {
        return postServiceImpl.getAllPosts();
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Post> getPostBySlug(@PathVariable String slug) {
        return postServiceImpl.getPostBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        return ResponseEntity.ok(postServiceImpl.createPost(post));
    }
}
