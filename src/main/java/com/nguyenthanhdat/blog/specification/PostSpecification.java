package com.nguyenthanhdat.blog.specification;

import com.nguyenthanhdat.blog.domain.entities.Post;
import com.nguyenthanhdat.blog.domain.PostStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
public class PostSpecification {
    public static Specification<Post> hasStatus(PostStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status != null) {
                return criteriaBuilder.equal(root.get("status"), status);
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

    public static Specification<Post> hasCategory(String category) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(category)) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("category").get("name")), "%" + category.toLowerCase() + "%");
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
