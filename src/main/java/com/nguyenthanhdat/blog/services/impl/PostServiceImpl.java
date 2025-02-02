package com.nguyenthanhdat.blog.services.impl;

import com.nguyenthanhdat.blog.domain.entities.Post;
import com.nguyenthanhdat.blog.repositories.PostRepository;
import com.nguyenthanhdat.blog.services.FileStorageService;
import com.nguyenthanhdat.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final FileStorageService fileStorageService;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post> getPostBySlug(String slug) {
        return postRepository.findBySlug(slug);
    }

    @Override
    public Post createPost(Post post, MultipartFile thumbnail, List<MultipartFile> contentImages) {
        if (thumbnail != null && !thumbnail.isEmpty()) {
            String thumbnailUrl = fileStorageService.uploadFile(thumbnail);
            post.setThumbnailUrl(thumbnailUrl);
        }

        if (contentImages != null && !contentImages.isEmpty()) {
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

}
