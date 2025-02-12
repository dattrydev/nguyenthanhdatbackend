package com.nguyenthanhdat.blog.controllers.blog;

import com.nguyenthanhdat.blog.domain.dtos.blog.post.BlogPostDto;
import com.nguyenthanhdat.blog.domain.dtos.blog.post.BlogPostListPagingDto;
import com.nguyenthanhdat.blog.exceptions.ResourceNotFoundException;
import com.nguyenthanhdat.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog/v1/posts")
@RequiredArgsConstructor
public class BlogPostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<BlogPostListPagingDto> getBlogPostList(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String tagsName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        BlogPostListPagingDto response = postService.getBlogPostList(
                title, description, categoryName, tagsName, page, size, sortBy, sortDirection
        ).orElseThrow(() -> new ResourceNotFoundException("No post found"));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<BlogPostDto> getBlogPostBySlug(@PathVariable String slug) {
        BlogPostDto response = postService.getBlogPostBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post " + slug + " not found"));

        return ResponseEntity.ok(response);
    }
}
