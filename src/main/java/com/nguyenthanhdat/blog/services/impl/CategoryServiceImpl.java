package com.nguyenthanhdat.blog.services.impl;

import com.nguyenthanhdat.blog.domain.entities.Category;
import com.nguyenthanhdat.blog.repositories.CategoryRepository;
import com.nguyenthanhdat.blog.services.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        if(categoryRepository.existsByNameIgnoreCase(category.getName())){
            throw new IllegalArgumentException("Category with name " + category.getName() + " already exists");
        } else {
            return categoryRepository.save(category);
        }
    }

    @Override
    @Transactional
    public Category updateCategory(Category category) {
        Optional<Category> existingCategory = categoryRepository.findById(category.getId());
        if(existingCategory.isPresent()){
            Category updatedCategory = existingCategory.get();
            updatedCategory.setName(category.getName());
            return categoryRepository.save(updatedCategory);
        } else {
            throw new IllegalArgumentException("Category with id " + category.getId() + " not found");
        }
    }

    @Override
    public void deleteCategory(UUID id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isPresent()){
            if (category.get().getPosts().isEmpty()) {
                categoryRepository.deleteById(id);
            } else {
                throw new IllegalArgumentException("Category with id " + id + " has posts");
            }
        } else {
            throw new IllegalArgumentException("Category with id " + id + " not found");
        }
    }
}
