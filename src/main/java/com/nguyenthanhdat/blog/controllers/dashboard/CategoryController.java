package com.nguyenthanhdat.blog.controllers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.CategoryListDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.CreateCategoryDto;
import com.nguyenthanhdat.blog.domain.entities.Category;
import com.nguyenthanhdat.blog.mappers.dashboard.DashboardCategoryMapper;
import com.nguyenthanhdat.blog.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final DashboardCategoryMapper dashboardCategoryMapper;
    @GetMapping
    public ResponseEntity<List<CategoryListDto>> getAllCategories() {
        List<CategoryListDto> categories = categoryService.listCategories()
                .stream().map(dashboardCategoryMapper::toDto)
                .toList();

        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryListDto> createCategory(@Valid @RequestBody CreateCategoryDto createCategoryDto) {
    Category categoryToCreate = dashboardCategoryMapper.toEntity(createCategoryDto);
    Category savedCategory = categoryService.createCategory(categoryToCreate);
        return new ResponseEntity<>(
            dashboardCategoryMapper.toDto(savedCategory),
            HttpStatus.CREATED
    );
    }

    @PatchMapping
    public ResponseEntity<CategoryListDto> updateCategory(@Valid @RequestBody Category category) {
        Category categoryToUpdate = categoryService.updateCategory(category);
        return new ResponseEntity<>(
            dashboardCategoryMapper.toDto(categoryToUpdate),
            HttpStatus.OK
        );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
