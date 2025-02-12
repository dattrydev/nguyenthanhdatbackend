package com.nguyenthanhdat.blog.domain.repositories;

import com.nguyenthanhdat.blog.domain.entities.Category;
import com.nguyenthanhdat.blog.domain.projections.blog.category.BlogCategoryProjection;
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
public interface CategoryRepository extends JpaRepository<Category, UUID>, JpaSpecificationExecutor<Category> {
    boolean existsByName(String name);
    boolean existsByNameIgnoreCase(String name);

    Optional<Category> findBySlug(@NotNull(message = "Category slug cannot be null") @Size(min = 3, max = 100, message = "Category slug must be between 3 and 100 characters") String slug);

    @Query(value = """
    SELECT c.name AS name, c.slug AS slug, COALESCE(COUNT(p.id), 0) AS postCount
    FROM categories c
    LEFT JOIN posts p ON c.id = p.category_id
    GROUP BY c.id, c.name, c.slug
""", nativeQuery = true)
    List<BlogCategoryProjection> findAllCategoriesWithPostCount();
}
