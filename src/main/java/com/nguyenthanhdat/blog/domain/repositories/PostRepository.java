package com.nguyenthanhdat.blog.domain.repositories;

import com.nguyenthanhdat.blog.domain.entities.Post;
import com.nguyenthanhdat.blog.domain.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID>, JpaSpecificationExecutor<Post> {
    Post findBySlug(String slug);
    Post findBySlugAndStatus(String slug, PostStatus status);
    Optional<Post> findByTitle(String title);
    boolean existsByTitle(String title);
    boolean existsBySlug(String slug);
    Page<Post> findByTitleContainingAndStatus(String title, String status, Pageable pageable);
    long countByTitleContainingAndStatus(String title, String status);
}
