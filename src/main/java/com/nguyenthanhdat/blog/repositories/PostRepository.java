package com.nguyenthanhdat.blog.repositories;

import com.nguyenthanhdat.blog.domain.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    Post findBySlug(String slug);
}
