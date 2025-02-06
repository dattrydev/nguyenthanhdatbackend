package com.nguyenthanhdat.blog.controllers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.*;
import com.nguyenthanhdat.blog.exceptions.ResourceNotFoundException;
import com.nguyenthanhdat.blog.mappers.dashboard.DashboardCategoryMapper;
import com.nguyenthanhdat.blog.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<DashboardCategoryListPagingDto> getDashboardCategoryList(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        Optional<DashboardCategoryListPagingDto> dashboardCategoryListPagingDto = categoryService.getDashboardCategoryList(name, page ,size, sortBy, sortDirection);

        return dashboardCategoryListPagingDto.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("No categories found"));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DashboardCategoryDto> getDashboardCategoryById(@PathVariable UUID id) {
        Optional<DashboardCategoryDto> dashboardCategoryDto = categoryService.getDashboardCategoryById(id);

        return dashboardCategoryDto.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @PostMapping
    public ResponseEntity<DashboardCreateCategoryDto> createCategory(@Valid @RequestBody DashboardCreateCategoryDto dashboardCreateCategoryDto) {
        Optional<DashboardCategoryDto> savedCategory = categoryService.createCategory(dashboardCreateCategoryDto);

        return savedCategory.map(category -> ResponseEntity.status(HttpStatus.CREATED).body(dashboardCreateCategoryDto))
                .orElseThrow(() -> new ResourceNotFoundException("Category could not be created"));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<DashboardCategoryDto> updateCategory(@PathVariable UUID id, @RequestBody DashboardUpdateCategoryDto dashboardUpdateCategoryDto) {
        Optional<DashboardCategoryDto> categoryToUpdate = categoryService.updateCategory(id, dashboardUpdateCategoryDto);

        return new ResponseEntity<>(
                categoryToUpdate.orElseThrow(() -> new ResourceNotFoundException("Category not found")),
            HttpStatus.OK
        );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
