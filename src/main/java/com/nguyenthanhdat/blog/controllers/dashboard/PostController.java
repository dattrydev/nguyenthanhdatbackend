package com.nguyenthanhdat.blog.controllers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardCreatePostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostListPagingDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardUpdatePostDto;
import com.nguyenthanhdat.blog.exceptions.ResourceCreationException;
import com.nguyenthanhdat.blog.exceptions.ResourceNotFoundException;
import com.nguyenthanhdat.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {

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

//    @GetMapping("/{field}")
//    public ResponseEntity<Void> validateField(@PathVariable String field, @RequestParam String value) {
//        validationService.validateField(field, value);
//        return ResponseEntity.ok().build();
//    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<DashboardPostDto> createPost(
            @ModelAttribute DashboardCreatePostDto dashboardCreatePostDto,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam(value = "contentImages", required = false) List<MultipartFile> contentImages
    ) {
        Optional<DashboardPostDto> savedPost = postService.createPost(dashboardCreatePostDto, thumbnail, contentImages);
        return savedPost.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceCreationException("Failed to create post"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DashboardUpdatePostDto> updatePost(
            @PathVariable UUID id,
            @ModelAttribute DashboardUpdatePostDto postDto,
            @RequestParam(value = "newThumbnail", required = false) MultipartFile newThumbnail,
            @RequestParam(value = "newContentImages", required = false) List<MultipartFile> newContentImages,
            @RequestParam(value = "oldThumbnail", required = false) String oldThumbnail,
            @RequestParam(value = "oldContentImages", required = false) List<String> oldContentImages
    ) {
        Optional<DashboardUpdatePostDto> updatedPost = postService.updatePost(id, postDto, newThumbnail, newContentImages, oldThumbnail, oldContentImages);
        return updatedPost.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Post " + id + " not found"));
    }

}
