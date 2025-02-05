package com.nguyenthanhdat.blog.specification;

import com.nguyenthanhdat.blog.domain.entities.Tag;
import org.springframework.data.jpa.domain.Specification;

public class TagSpecification {
    public static Specification<Tag > hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name != null) {
                return criteriaBuilder.equal(root.get("name"), name);
            }
            return null;
        };
    }
}
