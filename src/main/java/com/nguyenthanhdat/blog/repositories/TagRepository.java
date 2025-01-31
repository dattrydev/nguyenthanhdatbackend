package com.nguyenthanhdat.blog.repositories;

import com.nguyenthanhdat.blog.domain.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {
    Optional<Tag> findByName(String name);

    boolean existsByNameIgnoreCase(String name);
}
