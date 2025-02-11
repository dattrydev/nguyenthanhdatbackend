package com.nguyenthanhdat.blog.repositories;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardTagDto;
import com.nguyenthanhdat.blog.domain.entities.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID>, JpaSpecificationExecutor<Tag> {
    boolean existsByName(String name);
    boolean existsByNameIgnoreCase(String name);

    Optional<Tag> findBySlug(@NotNull(message = "Tag slug cannot be null") @Size(min = 3, max = 100, message = "Tag slug must be between 3 and 100 characters") String slug);
}
