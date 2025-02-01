package com.nguyenthanhdat.blog.services;

import com.nguyenthanhdat.blog.domain.entities.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> listCategories();
    Category createCategory(Category category);
    Category updateCategory(Category category);
    void deleteCategory(UUID id);
}
