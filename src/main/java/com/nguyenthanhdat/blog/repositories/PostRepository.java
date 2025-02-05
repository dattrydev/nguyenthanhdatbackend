package com.nguyenthanhdat.blog.repositories;

import com.nguyenthanhdat.blog.domain.entities.Post;
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
    Optional<Post> findByTitle(String title);
    boolean existsByTitle(String title);
    Page<Post> findByTitleContainingAndStatus(String title, String status, Pageable pageable);
    long countByTitleContainingAndStatus(String title, String status);
}
