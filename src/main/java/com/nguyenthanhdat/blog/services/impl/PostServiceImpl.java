package com.nguyenthanhdat.blog.services.impl;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.*;
import com.nguyenthanhdat.blog.domain.enums.PostStatus;
import com.nguyenthanhdat.blog.domain.entities.Category;
import com.nguyenthanhdat.blog.domain.entities.Post;
import com.nguyenthanhdat.blog.domain.entities.Tag;
import com.nguyenthanhdat.blog.exceptions.FileUploadException;
import com.nguyenthanhdat.blog.exceptions.ResourceAlreadyExistsException;
import com.nguyenthanhdat.blog.exceptions.ResourceNotFoundException;
import com.nguyenthanhdat.blog.mappers.dashboard.DashboardPostMapper;
import com.nguyenthanhdat.blog.repositories.CategoryRepository;
import com.nguyenthanhdat.blog.repositories.PostRepository;
import com.nguyenthanhdat.blog.repositories.TagRepository;
import com.nguyenthanhdat.blog.services.FileStorageService;
import com.nguyenthanhdat.blog.services.PostService;
import com.nguyenthanhdat.blog.specification.PostSpecification;
import com.nguyenthanhdat.blog.utils.PresignedUrl;
import com.nguyenthanhdat.blog.utils.ReadingTime;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static com.nguyenthanhdat.blog.utils.GenerateSlug.generateSlug;

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
    public Optional<DashboardPostListPagingDto> getDashboardPostList(String title, String status, Integer readingTime, String category, int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Specification<Post> specification = Specification.where(PostSpecification.hasStatus(status != null ? PostStatus.valueOf(status) : null))
                .and(PostSpecification.hasTitle(title))
                .and(PostSpecification.hasReadingTime(readingTime))
                .and(PostSpecification.hasCategory(category));

        Page<Post> postPage = postRepository.findAll(specification, pageable);

        List<DashboardPostListDto> postDtos = postPage.stream()
                .map(dashboardPostMapper::toPostListDto)
                .collect(Collectors.toList());

        long totalRecords = postRepository.count(specification);
        int totalPages = postPage.getTotalPages();

        DashboardPostListPagingDto dashboardPostListPagingDto = DashboardPostListPagingDto.builder()
                .posts(postDtos)
                .totalRecords(totalRecords)
                .totalPages(totalPages)
                .currentPage(page)
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
    @Transactional
    public Optional<DashboardPostDto> createPost(DashboardCreatePostDto dashboardCreatePostDto, MultipartFile thumbnail, List<MultipartFile> contentImages) {
        String title = dashboardCreatePostDto.getTitle();
        if (postRepository.existsByTitle(title)) {
            throw new ResourceAlreadyExistsException("Post with title '" + title + "' already exists.");
        }

        String generatedSlug = generateSlug(title);
        if (postRepository.existsBySlug(generatedSlug)) {
            throw new ResourceAlreadyExistsException("Post with slug '" + generatedSlug + "' already exists.");
        }

        Category category = categoryRepository.findById(dashboardCreatePostDto.getCategory_id())
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID '" + dashboardCreatePostDto.getCategory_id() + "' not found."));

        Set<Tag> tags = dashboardCreatePostDto.getTag_ids().stream()
                .map(tagId -> tagRepository.findById(tagId)
                        .orElseThrow(() -> new ResourceNotFoundException("Tag with ID '" + tagId + "' not found.")))
                .collect(Collectors.toSet());

        int readingTime = ReadingTime.calculateReadingTime(dashboardCreatePostDto.getContent());

        Post post = Post.builder()
                .title(title)
                .slug(generatedSlug)
                .content(dashboardCreatePostDto.getContent())
                .readingTime(readingTime)
                .status(dashboardCreatePostDto.getStatus())
                .category(category)
                .tags(tags)
                .build();

        if (thumbnail != null && !thumbnail.isEmpty()) {
            try {
                String thumbnailUrl = fileStorageService.uploadFile(thumbnail);
                post.setThumbnailUrl(thumbnailUrl);
            } catch (Exception e) {
                throw new FileUploadException("Error uploading thumbnail: " + e.getMessage());
            }
        }

        if (contentImages != null && !contentImages.isEmpty()) {
            Set<String> imageUrls = new HashSet<>();
            for (MultipartFile image : contentImages) {
                if (!image.isEmpty()) {
                    try {
                        imageUrls.add(fileStorageService.uploadFile(image));
                    } catch (Exception e) {
                        throw new FileUploadException("Error uploading content images: " + e.getMessage());
                    }
                }
            }
            post.setContentImages(imageUrls);
        }

        post = postRepository.save(post);

        UUID postId = post.getId();

        return Optional.of(dashboardPostMapper.toDto(post)).map(dto -> {
            dto.setId(postId);
            return dto;
        });
    }

    @Override
    @Transactional
    public Optional<DashboardPostDto> updatePost(UUID id, DashboardUpdatePostDto dashboardUpdatePostDto, MultipartFile newThumbnail,
                                                 List<MultipartFile> newContentImages, String oldThumbnail,
                                                 List<String> oldContentImages) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post " + id + " not found"));

        if (dashboardUpdatePostDto.getTitle() != null) {
            post.setTitle(dashboardUpdatePostDto.getTitle());
        }
        if (dashboardUpdatePostDto.getContent() != null) {
            post.setContent(dashboardUpdatePostDto.getContent());
            post.setReadingTime(ReadingTime.calculateReadingTime(dashboardUpdatePostDto.getContent()));
        }
        if (dashboardUpdatePostDto.getStatus() != null) {
            post.setStatus(dashboardUpdatePostDto.getStatus());
        }

        if (dashboardUpdatePostDto.getCategory_id() != null) {
            Category category = categoryRepository.findById(dashboardUpdatePostDto.getCategory_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Category " + dashboardUpdatePostDto.getCategory_id() + " not found"));
            post.setCategory(category);
        }

        if (dashboardUpdatePostDto.getTag_ids() != null && !dashboardUpdatePostDto.getTag_ids().isEmpty()) {
            Set<Tag> tags = dashboardUpdatePostDto.getTag_ids().stream()
                    .map(tag_id -> tagRepository.findById(tag_id)
                            .orElseThrow(() -> new ResourceNotFoundException("Tag " + tag_id + " not found")))
                    .collect(Collectors.toSet());
            post.setTags(tags);
        }

        if (newThumbnail != null && !newThumbnail.isEmpty()) {
            if (post.getThumbnailUrl() != null && !post.getThumbnailUrl().equals(oldThumbnail)) {
                fileStorageService.deleteFile(post.getThumbnailUrl());
            }
            post.setThumbnailUrl(fileStorageService.uploadFile(newThumbnail));
        } else if (oldThumbnail != null) {
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

        post = postRepository.save(post);

        UUID postId = post.getId();

        return Optional.of(dashboardPostMapper.toDto(post)).map(dto -> {
            dto.setId(postId);
            return dto;
        });
    }

    @Override
    public boolean isFieldExists(String field, String value) {
        switch (field.toLowerCase()) {
            case "title":
                return postRepository.existsByTitle(value);
            default:
                throw new IllegalArgumentException("Invalid field: " + field);
        }
    }


}
