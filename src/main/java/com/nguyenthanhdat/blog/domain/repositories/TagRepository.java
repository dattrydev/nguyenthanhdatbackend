package com.nguyenthanhdat.blog.domain.repositories;

import com.nguyenthanhdat.blog.domain.entities.Tag;
import com.nguyenthanhdat.blog.domain.projections.blog.tag.BlogTagProjection;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID>, JpaSpecificationExecutor<Tag> {
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    Optional<Tag> findBySlug(@NotNull(message = "Tag slug cannot be null") @Size(min = 3, max = 100, message = "Tag slug must be between 3 and 100 characters") String slug);

    @Query(value = """
    SELECT t.name AS name, t.slug AS slug, COALESCE(COUNT(pt.post_id), 0) AS postCount
    FROM tags t
    LEFT JOIN post_tags pt ON t.id = pt.tag_id
    GROUP BY t.id, t.name, t.slug
    """, nativeQuery = true)
    List<BlogTagProjection> findAllTagsWithPostCount();
}