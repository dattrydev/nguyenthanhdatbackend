package com.nguyenthanhdat.blog.specification;

import com.nguyenthanhdat.blog.domain.entities.Post;
import com.nguyenthanhdat.blog.domain.enums.PostStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PostSpecification {
    public static Specification<Post> hasStatus(List<PostStatus> statuses) {
        return (root, query, criteriaBuilder) -> {
            if (statuses != null && !statuses.isEmpty()) {
                return root.get("status").in(statuses);
            }
            return null;
        };
    }

    public static Specification<Post> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(title)) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
            }
            return null;
        };
    }

    public static Specification<Post> hasDescription(String description) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(description)) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
            }
            return null;
        };
    }

    public static Specification<Post> hasCategory(List<String> category) {
        return (root, query, criteriaBuilder) -> {
            if (category != null && !category.isEmpty()) {
                List<UUID> categoryIds = category.stream()
                        .map(UUID::fromString)
                        .collect(Collectors.toList());

                return root.get("category").get("id").in(categoryIds);
            }
            return null;
        };
    }

    public static Specification<Post> hasCategorySlug(String categorySlug) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(categorySlug)) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("category").get("slug")), "%" + categorySlug.toLowerCase() + "%");
            }
            return null;
        };
    }

    public static Specification<Post> hasTags(List<String> tags) {
        return (root, query, criteriaBuilder) -> {
            if (tags != null && !tags.isEmpty()) {
                List<UUID> tagIds = tags.stream()
                        .map(UUID::fromString)
                        .collect(Collectors.toList());

                return root.join("tags").get("id").in(tagIds);
            }
            return null;
        };
    }

    public static Specification<Post> hasTagsSlug(String tagsSlug) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(tagsSlug)) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.join("tags").get("slug")), "%" + tagsSlug.toLowerCase() + "%");
            }
            return null;
        };
    }

    public static Specification<Post> hasReadingTime(Integer readingTime) {
        return (root, query, criteriaBuilder) -> {
            if (readingTime != null) {
                return criteriaBuilder.equal(root.get("readingTime"), readingTime);
            }
            return null;
        };
    }
}
