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

import java.util.*;

@RestController
@RequestMapping("/api/dashboard/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<DashboardPostListPagingDto> getDashboardPostList(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer reading_time,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tags,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        page = page - 1;

        List<String> statusList = (status != null && !status.isEmpty()) ? Arrays.asList(status.split(",")) : Collections.emptyList();
        List<String> categoryList = (category != null && !category.isEmpty()) ? Arrays.asList(category.split(",")) : Collections.emptyList();
        List<String> tagsList = (tags != null && !tags.isEmpty()) ? Arrays.asList(tags.split(",")) : Collections.emptyList();

        Optional<DashboardPostListPagingDto> response = postService.getDashboardPostList(
                title, statusList, reading_time, categoryList, tagsList, page, size, sortBy, sortDirection
        );

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMultiplePosts(@RequestBody List<UUID> ids) {
        postService.deletePosts(ids);
        return ResponseEntity.noContent().build();
    }
}
