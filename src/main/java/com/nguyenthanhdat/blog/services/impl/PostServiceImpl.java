package com.nguyenthanhdat.blog.services.impl;

import com.nguyenthanhdat.blog.domain.dtos.PaginationDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.*;
import com.nguyenthanhdat.blog.domain.enums.PostStatus;
import com.nguyenthanhdat.blog.domain.entities.Category;
import com.nguyenthanhdat.blog.domain.entities.Post;
import com.nguyenthanhdat.blog.domain.entities.Tag;
import com.nguyenthanhdat.blog.exceptions.dashboard.error.FileUploadException;
import com.nguyenthanhdat.blog.exceptions.dashboard.category.CategoryNotFoundException;
import com.nguyenthanhdat.blog.exceptions.dashboard.post.PostAlreadyExistsException;
import com.nguyenthanhdat.blog.exceptions.dashboard.tag.TagNotFoundException;
import com.nguyenthanhdat.blog.mappers.dashboard.DashboardPostMapper;
import com.nguyenthanhdat.blog.repositories.CategoryRepository;
import com.nguyenthanhdat.blog.repositories.PostRepository;
import com.nguyenthanhdat.blog.repositories.TagRepository;
import com.nguyenthanhdat.blog.services.FileStorageService;
import com.nguyenthanhdat.blog.services.PostService;
import com.nguyenthanhdat.blog.specification.PostSpecification;
import com.nguyenthanhdat.blog.utils.PresignedUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final DashboardPostMapper dashboardPostMapper;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final FileStorageService fileStorageService;
    private final PresignedUrl presignedUrl;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public Optional<DashboardPostListPagingDto> getDashboardPostList(String title, String status, Integer readingTime, String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Post> specification = Specification.where(PostSpecification.hasStatus(status != null ? PostStatus.valueOf(status) : null))
                .and(PostSpecification.hasTitle(title))
                .and(PostSpecification.hasReadingTime(readingTime))
                .and(PostSpecification.hasCategory(category));

        Page<Post> postPage = postRepository.findAll(specification, pageable);

        List<DashboardPostListDto> postDtos = postPage.stream()
                .map(dashboardPostMapper::toPostListDto)
                .collect(Collectors.toList());

        long totalRecords = postRepository.count(specification);

        DashboardPostListPagingDto dashboardPostListPagingDto = DashboardPostListPagingDto.builder()
                .posts(postDtos)
                .paginationDto(new PaginationDto(totalRecords, page, size))
                .build();

        return Optional.of(dashboardPostListPagingDto);
    }

    @Override
    public Optional<DashboardPostDto> getPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug);
        if (post != null) {
            if (post.getThumbnailUrl() != null) {
                String thumbnailUrl = presignedUrl.createPresignedGetUrl(bucketName, post.getThumbnailUrl());
                post.setThumbnailUrl(thumbnailUrl);
            }

            if (post.getContentImages() != null) {
                Set<String> contentImages = new HashSet<>();
                for (String imageUrl : post.getContentImages()) {
                    contentImages.add(presignedUrl.createPresignedGetUrl(bucketName, imageUrl));
                }
                post.setContentImages(contentImages);
            }
        }

        return Optional.ofNullable(post)
                .map(dashboardPostMapper::toDto);
    }

    @Override
    public Optional<DashboardPostDto> createPost(DashboardCreatePostDto dashboardCreatePostDto, MultipartFile thumbnail, List<MultipartFile> contentImages) {
        if (postRepository.existsByTitle(dashboardCreatePostDto.getTitle())) {
            throw new PostAlreadyExistsException("Post with the same title already exists.");
        }

        Category category = categoryRepository.findById(dashboardCreatePostDto.getCategory_id())
                .orElseThrow(() -> new CategoryNotFoundException("Category " + dashboardCreatePostDto.getCategory_id() + " not found"));

        Set<Tag> tags = dashboardCreatePostDto.getTag_ids().stream()
                .map(tagId -> tagRepository.findById(tagId)
                        .orElseThrow(() -> new TagNotFoundException("Tag " + tagId + " not found")))
                .collect(Collectors.toSet());

        Post post = Post.builder()
                .title(dashboardCreatePostDto.getTitle())
                .content(dashboardCreatePostDto.getContent())
                .readingTime(dashboardCreatePostDto.getReadingTime())
                .status(dashboardCreatePostDto.getStatus())
                .category(category)
                .tags(tags)
                .build();

        if (thumbnail != null && !thumbnail.isEmpty()) {
            try {
                String thumbnailUrl = fileStorageService.uploadFile(thumbnail);
                post.setThumbnailUrl(thumbnailUrl);
            } catch (Exception e) {
                throw new FileUploadException("Error uploading thumbnail");
            }
        }

        if (contentImages != null && !contentImages.isEmpty()) {
            Set<String> imageUrls = new HashSet<>();
            for (MultipartFile image : contentImages) {
                if (!image.isEmpty()) {
                    try {
                        imageUrls.add(fileStorageService.uploadFile(image));
                    } catch (Exception e) {
                        throw new FileUploadException("Error uploading content images");
                    }
                }
            }
            post.setContentImages(imageUrls);
        }

        post = postRepository.save(post);

        return Optional.of(post)
                .map(dashboardPostMapper::toDto);
    }

    @Override
    public Optional<DashboardUpdatePostDto> updatePost(String slug, DashboardUpdatePostDto dashboardUpdatePostDto, MultipartFile newThumbnail,
                           List<MultipartFile> newContentImages, String oldThumbnail,
                           List<String> oldContentImages) {
        Post post = postRepository.findBySlug(slug);
        if (post == null) {
            throw new RuntimeException("Post not found");
        }

        post.setTitle(dashboardUpdatePostDto.getTitle());
        post.setContent(dashboardUpdatePostDto.getContent());
        post.setReadingTime(dashboardUpdatePostDto.getReadingTime());
        post.setStatus(dashboardUpdatePostDto.getStatus());

        Category category = categoryRepository.findById(dashboardUpdatePostDto.getCategory_id())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        post.setCategory(category);

        Set<Tag> tags = dashboardUpdatePostDto.getTag_ids().stream()
                .map(tag_id -> tagRepository.findById(tag_id)
                        .orElseThrow(() -> new RuntimeException("Tag not found")))
                .collect(Collectors.toSet());
        post.setTags(tags);

        if (newThumbnail != null && !newThumbnail.isEmpty()) {
            if (post.getThumbnailUrl() != null && !post.getThumbnailUrl().equals(oldThumbnail)) {
                fileStorageService.deleteFile(post.getThumbnailUrl());
            }
            String thumbnailUrl = fileStorageService.uploadFile(newThumbnail);
            post.setThumbnailUrl(thumbnailUrl);
        } else {
            post.setThumbnailUrl(oldThumbnail);
        }

        Set<String> newImageUrls = new HashSet<>();
        if (newContentImages != null && !newContentImages.isEmpty()) {
            for (MultipartFile image : newContentImages) {
                if (!image.isEmpty()) {
                    newImageUrls.add(fileStorageService.uploadFile(image));
                }
            }
        }

        if (oldContentImages != null) {
            newImageUrls.addAll(oldContentImages);
        }

        Set<String> imagesToDelete = new HashSet<>(post.getContentImages());
        imagesToDelete.removeAll(newImageUrls);
        for (String oldImageUrl : imagesToDelete) {
            fileStorageService.deleteFile(oldImageUrl);
        }

        post.setContentImages(newImageUrls);

        postRepository.save(post);

        return Optional.of(dashboardPostMapper.toUpdatePostDto(post));
    }

}
