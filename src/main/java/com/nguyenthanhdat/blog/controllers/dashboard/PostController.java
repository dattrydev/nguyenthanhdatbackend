package com.nguyenthanhdat.blog.controllers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardCreatePostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardUpdatePostDto;
import com.nguyenthanhdat.blog.exceptions.dashboard.post.PostNotFoundException;
import com.nguyenthanhdat.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getPostList(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer readingTime,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Map<String, Object> response = postService.getDashboardPostList(title, status, readingTime, category, page, size);

        if (response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No posts found"));
        }

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<DashboardPostDto> getPostBySlug(@PathVariable String slug) {
        return postService.getPostBySlug(slug)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PostNotFoundException("Post " + slug + " not found"));
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<DashboardPostDto> createPost(
            @ModelAttribute DashboardCreatePostDto dashboardCreatePostDto,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam(value = "contentImages", required = false) List<MultipartFile> contentImages
    ) {
        DashboardPostDto savedPost = postService.createPost(dashboardCreatePostDto, thumbnail, contentImages);
        return ResponseEntity.ok(savedPost);
    }

    @PatchMapping("/{slug}")
    public ResponseEntity<DashboardUpdatePostDto> updatePost(
            @PathVariable String slug,
            @ModelAttribute DashboardUpdatePostDto postDto,
            @RequestParam(value = "newThumbnail", required = false) MultipartFile newThumbnail,
            @RequestParam(value = "newContentImages", required = false) List<MultipartFile> newContentImages,
            @RequestParam(value = "oldThumbnail", required = false) String oldThumbnail,
            @RequestParam(value = "oldContentImages", required = false) List<String> oldContentImages
    ) {
        DashboardUpdatePostDto updatedPost = postService.updatePost(slug, postDto, newThumbnail, newContentImages, oldThumbnail, oldContentImages);
        return ResponseEntity.ok(updatedPost);
    }

}
