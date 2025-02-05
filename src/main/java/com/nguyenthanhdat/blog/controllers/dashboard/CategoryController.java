package com.nguyenthanhdat.blog.controllers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.DashboardCategoryListDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.DashboardCreateCategoryDto;
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
    public ResponseEntity<List<DashboardCategoryListDto>> getCategoryList(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<DashboardCategoryListDto> categories = categoryService.listCategories()
                .stream().map(dashboardCategoryMapper::toDto)
                .toList();

        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<DashboardCategoryListDto> createCategory(@Valid @RequestBody DashboardCreateCategoryDto dashboardCreateCategoryDto) {
    Category categoryToCreate = dashboardCategoryMapper.toEntity(dashboardCreateCategoryDto);
    Category savedCategory = categoryService.createCategory(categoryToCreate);
        return new ResponseEntity<>(
            dashboardCategoryMapper.toDto(savedCategory),
            HttpStatus.CREATED
    );
    }

    @PatchMapping
    public ResponseEntity<DashboardCategoryListDto> updateCategory(@Valid @RequestBody Category category) {
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
