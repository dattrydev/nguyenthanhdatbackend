package com.nguyenthanhdat.blog.controllers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.CreatePostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardUpdatePostDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.PostDto;
import com.nguyenthanhdat.blog.domain.entities.Post;
import com.nguyenthanhdat.blog.mappers.dashboard.DashboardPostMapper;
import com.nguyenthanhdat.blog.services.PostService;
import lombok.RequiredArgsConstructor;
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
    public Map<String, Object> getFilteredPosts(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer readingTime,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return postService.getAllDashboardPosts(title, status, readingTime, category, page, size);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<PostDto> getPostBySlug(@PathVariable String slug) {
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
