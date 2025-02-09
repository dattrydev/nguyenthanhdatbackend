package com.nguyenthanhdat.blog.controllers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardCreatePostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostListPagingDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardUpdatePostDto;
import com.nguyenthanhdat.blog.exceptions.ResourceCreationException;
import com.nguyenthanhdat.blog.exceptions.ResourceNotFoundException;
import com.nguyenthanhdat.blog.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<DashboardPostListPagingDto> getDashboardPostList(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer readingTime,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        page = page - 1;
        Optional<DashboardPostListPagingDto> response = postService.getDashboardPostList(title, status, readingTime, category, page, size, sortBy, sortDirection);

        return response.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("No post found"));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<DashboardPostDto> getDashboardPostBySlug(@PathVariable String slug) {
        return postService.getPostBySlug(slug)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Post " + slug + " not found"));
    }

    @GetMapping("/check-unique")
    public ResponseEntity<Boolean> checkUniqueField(
            @RequestParam String field,
            @RequestParam String value) {
        boolean exists = postService.isFieldExists(field, value);
        return ResponseEntity.ok(!exists);
    }

    @PostMapping
    public ResponseEntity<DashboardPostDto> createPost(
            @Valid @RequestBody DashboardCreatePostDto dashboardCreatePostDto
    ) {
        Optional<DashboardPostDto> savedPost = postService.createPost(dashboardCreatePostDto);
        return savedPost.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceCreationException("Failed to create post"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DashboardPostDto> updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody DashboardUpdatePostDto postDto
    ) {
        Optional<DashboardPostDto> updatedPost = postService.updatePost(id, postDto);
        return updatedPost.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Post " + id + " not found"));
    }
}
