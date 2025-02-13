package com.nguyenthanhdat.blog.services.impl;

import com.nguyenthanhdat.blog.domain.dtos.blog.post.BlogPostDto;
import com.nguyenthanhdat.blog.domain.dtos.blog.post.BlogPostListDto;
import com.nguyenthanhdat.blog.domain.dtos.blog.post.BlogPostListPagingDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.*;
import com.nguyenthanhdat.blog.domain.enums.PostStatus;
import com.nguyenthanhdat.blog.domain.entities.Category;
import com.nguyenthanhdat.blog.domain.entities.Post;
import com.nguyenthanhdat.blog.domain.entities.Tag;
import com.nguyenthanhdat.blog.exceptions.ResourceAlreadyExistsException;
import com.nguyenthanhdat.blog.exceptions.ResourceNotFoundException;
import com.nguyenthanhdat.blog.mappers.blog.BlogPostMapper;
import com.nguyenthanhdat.blog.mappers.dashboard.DashboardPostMapper;
import com.nguyenthanhdat.blog.domain.repositories.CategoryRepository;
import com.nguyenthanhdat.blog.domain.repositories.PostRepository;
import com.nguyenthanhdat.blog.domain.repositories.TagRepository;
import com.nguyenthanhdat.blog.services.PostService;
import com.nguyenthanhdat.blog.specification.PostSpecification;
import com.nguyenthanhdat.blog.utils.PresignedUrl;
import com.nguyenthanhdat.blog.utils.ReadingTime;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.nguyenthanhdat.blog.utils.SlugGenerator.generateSlug;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final DashboardPostMapper dashboardPostMapper;
    private final BlogPostMapper blogPostMapper;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final PresignedUrl presignedUrl;

    @Override
    public Optional<DashboardPostListPagingDto> getDashboardPostList(
            String title, List<String> status, Integer readingTime, List<String> category, List<String> tags,
            int page, int size, String sortBy, String sortDirection) {
        page = page - 1;
        if (page < 0) {
            throw new IllegalArgumentException("Page number must be greater than 0");
        }

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        List<PostStatus> statusList = status.stream()
                .map(PostStatus::valueOf)
                .collect(Collectors.toList());

        Specification<Post> specification = Specification.where(PostSpecification.hasStatus(statusList))
                .and(PostSpecification.hasTitle(title))
                .and(PostSpecification.hasReadingTime(readingTime))
                .and(PostSpecification.hasCategory(category))
                .and(PostSpecification.hasTags(tags));

        Page<Post> postPage = postRepository.findAll(specification, pageable);

        List<DashboardPostListDto> postDtos = postPage.stream()
                .map(dashboardPostMapper::toPostListDto)
                .collect(Collectors.toList());

        int totalPages = postPage.getTotalPages();

        DashboardPostListPagingDto dashboardPostListPagingDto = DashboardPostListPagingDto.builder()
                .posts(postDtos)
                .totalPages(totalPages)
                .currentPage(page + 1)
                .build();

        return Optional.of(dashboardPostListPagingDto);
    }


    @Override
    public Optional<DashboardPostDto> getPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug);

        String convertedContent = presignedUrl.convertKeyToPresignedUrl(post.getContent());

        post.setContent(convertedContent);

        return Optional.of(post)
                .map(dashboardPostMapper::toDto);
    }

    @Override
    @Transactional
    public Optional<DashboardPostDto> createPost(DashboardCreatePostDto dashboardCreatePostDto) {
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

        Set<Tag> tags = dashboardCreatePostDto.getTags_id().stream()
                .map(tagId -> tagRepository.findById(tagId)
                        .orElseThrow(() -> new ResourceNotFoundException("Tag with ID '" + tagId + "' not found.")))
                .collect(Collectors.toSet());

        int readingTime = ReadingTime.calculateReadingTime(dashboardCreatePostDto.getContent());

        Post post = Post.builder()
                .title(title)
                .slug(generatedSlug)
                .description(dashboardCreatePostDto.getDescription())
                .content(dashboardCreatePostDto.getContent())
                .readingTime(readingTime)
                .status(dashboardCreatePostDto.getStatus())
                .category(category)
                .tags(tags)
                .build();

        post = postRepository.save(post);

        UUID postId = post.getId();

        return Optional.of(dashboardPostMapper.toDto(post)).map(dto -> {
            dto.setId(postId);
            return dto;
        });
    }

    @Override
    @Transactional
    public Optional<DashboardPostDto> updatePost(UUID id, DashboardUpdatePostDto dashboardUpdatePostDto) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post " + id + " not found"));

        String convertedContent = presignedUrl.convertPresignedUrlToKey(dashboardUpdatePostDto.getContent());

        if (dashboardUpdatePostDto.getTitle() != null && !dashboardUpdatePostDto.getTitle().equals(post.getTitle())) {
            post.setTitle(dashboardUpdatePostDto.getTitle());

            String generatedSlug = generateSlug(post.getTitle());
            if (postRepository.existsBySlug(generatedSlug)) {
                throw new ResourceAlreadyExistsException("Post with slug '" + generatedSlug + "' already exists.");
            }
            post.setSlug(generatedSlug);
        }

        if(dashboardUpdatePostDto.getDescription() != null){
            post.setDescription(dashboardUpdatePostDto.getDescription());
        }

        if (dashboardUpdatePostDto.getContent() != null) {
            post.setContent(convertedContent);
            post.setReadingTime(ReadingTime.calculateReadingTime(dashboardUpdatePostDto.getContent()));
        }

        if (dashboardUpdatePostDto.getStatus() != null) {
            post.setStatus(dashboardUpdatePostDto.getStatus());
        }

        if (dashboardUpdatePostDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dashboardUpdatePostDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category " + dashboardUpdatePostDto.getCategoryId() + " not found"));
            post.setCategory(category);
        }

        if (dashboardUpdatePostDto.getTagsId() != null && !dashboardUpdatePostDto.getTagsId().isEmpty()) {
            Set<Tag> tags = dashboardUpdatePostDto.getTagsId().stream()
                    .map(tag_id -> tagRepository.findById(tag_id)
                            .orElseThrow(() -> new ResourceNotFoundException("Tag " + tag_id + " not found")))
                    .collect(Collectors.toSet());
            post.setTags(tags);
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
    public void deletePost(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post " + id + " not found"));
        postRepository.delete(post);
    }

    @Override
    @Transactional
    public void deletePosts(List<UUID> ids) {
        for (UUID id : ids) {
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Post " + id + " not found"));
            postRepository.delete(post);
        }
    }


    @Override
    public boolean isFieldExists(String field, String value) {
        if (field.equalsIgnoreCase("title")) {
            return postRepository.existsByTitle(value);
        }
        throw new IllegalArgumentException("Invalid field: " + field);
    }

    @Override
    public Optional<BlogPostListPagingDto> getBlogPostList(String title, String description, String categorySlug, String tagsSlug, int page, int size, String sortBy, String sortDirection) {
        page = page - 1;
        if (page < 0) {
            throw new IllegalArgumentException("Page number must be greater than 0");
        }

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Specification<Post> specification = Specification.where(PostSpecification.hasTitle(title))
                .and(PostSpecification.hasDescription(description))
                .and(PostSpecification.hasCategorySlug(categorySlug))
                .and(PostSpecification.hasTagsSlug(tagsSlug));

        Page<Post> postPage = postRepository.findAll(specification, pageable);

        List<BlogPostListDto> postDtos = postPage.stream()
                .map(blogPostMapper::toBlogPostListDto)
                .collect(Collectors.toList());

        int totalPages = postPage.getTotalPages();

        BlogPostListPagingDto blogPostListPagingDto = BlogPostListPagingDto.builder()
                .posts(postDtos)
                .totalPages(totalPages)
                .currentPage(page + 1)
                .build();

        return Optional.of(blogPostListPagingDto);
    }

    @Override
    public Optional<BlogPostDto> getBlogPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug);

        String convertedContent = presignedUrl.convertKeyToPresignedUrl(post.getContent());

        post.setContent(convertedContent);

        return Optional.of(post)
                .map(blogPostMapper::toBlogPostDto);
    }

    @Override
    public Optional<BlogPostListPagingDto> searchBlogPost(String keyword) {

        Specification<Post> specification = Specification.where(PostSpecification.hasTitle(keyword))
                .or(PostSpecification.hasDescription(keyword))
                .or(PostSpecification.hasCategorySlug(keyword))
                .or(PostSpecification.hasTagsSlug(keyword));

        List<Post> postList = postRepository.findAll(specification);

        if (postList.isEmpty()) {
            return Optional.empty();
        }

        List<BlogPostListDto> postDtos = postList.stream()
                .map(blogPostMapper::toBlogPostListDto)
                .collect(Collectors.toList());

        return Optional.of(new BlogPostListPagingDto(postDtos));
    }
}
