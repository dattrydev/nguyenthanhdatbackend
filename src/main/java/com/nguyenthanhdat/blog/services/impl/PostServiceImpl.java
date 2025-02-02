package com.nguyenthanhdat.blog.services.impl;

import com.nguyenthanhdat.blog.domain.dtos.post.CreatePostDto;
import com.nguyenthanhdat.blog.domain.entities.Category;
import com.nguyenthanhdat.blog.domain.entities.Post;
import com.nguyenthanhdat.blog.domain.entities.Tag;
import com.nguyenthanhdat.blog.repositories.CategoryRepository;
import com.nguyenthanhdat.blog.repositories.PostRepository;
import com.nguyenthanhdat.blog.repositories.TagRepository;
import com.nguyenthanhdat.blog.services.FileStorageService;
import com.nguyenthanhdat.blog.services.PostService;
import com.nguyenthanhdat.blog.utils.PresignedUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final FileStorageService fileStorageService;
    private final PresignedUrl presignedUrl;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public List<Post> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        for (Post post : posts) {
            if (post.getThumbnailUrl() != null) {
                String thumbnailUrl = presignedUrl.createPresignedGetUrl(bucketName, post.getThumbnailUrl());
                post.setThumbnailUrl(thumbnailUrl);
            }
        }

        return posts;
    }

    @Override
    public Optional<Post> getPostBySlug(String slug) {
        return postRepository.findBySlug(slug);
    }

    @Override
    public Post createPost(CreatePostDto createPostDto, MultipartFile thumbnail, List<MultipartFile> contentImages) {
        Category category = categoryRepository.findById(createPostDto.getCategory_id())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Set<Tag> tags = createPostDto.getTag_ids().stream()
                .map(tagId -> tagRepository.findById(tagId)
                        .orElseThrow(() -> new RuntimeException("Tag not found")))
                .collect(Collectors.toSet());

        Post post = Post.builder()
                .title(createPostDto.getTitle())
                .content(createPostDto.getContent())
                .readingTime(createPostDto.getReadingTime())
                .status(createPostDto.getStatus())
                .category(category)
                .tags(tags)
                .build();

        if (thumbnail != null && !thumbnail.isEmpty()) {
            Logger.getGlobal().info("thumbnail is not null");
            String thumbnailUrl = fileStorageService.uploadFile(thumbnail);
            post.setThumbnailUrl(thumbnailUrl);
        }

        if (contentImages != null && !contentImages.isEmpty()) {
            Logger.getGlobal().info("contentImages is not null");
            Set<String> imageUrls = new HashSet<>();
            for (MultipartFile image : contentImages) {
                if (!image.isEmpty()) {
                    imageUrls.add(fileStorageService.uploadFile(image));
                }
            }
            post.setContentImages(imageUrls);
        }

        return postRepository.save(post);
    }

    @Override
    public Post createPostWithoutImage(Post post) {
        return postRepository.save(post);
    }

}
