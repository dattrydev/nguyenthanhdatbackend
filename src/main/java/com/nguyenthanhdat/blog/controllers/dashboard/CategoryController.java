package com.nguyenthanhdat.blog.controllers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.*;
import com.nguyenthanhdat.blog.exceptions.ResourceNotFoundException;
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
            @Valid @RequestParam(required = false) String name,
            @Valid @RequestParam(defaultValue = "1") int page,
            @Valid @RequestParam(defaultValue = "10") int size,
            @Valid @RequestParam(defaultValue = "name") String sortBy,
            @Valid @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        page = page - 1;
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

    @GetMapping("/check-unique")
    public ResponseEntity<Boolean> checkUniqueField(
            @RequestParam String field,
            @RequestParam String value) {
        boolean exists = categoryService.isFieldExists(field, value);
        return ResponseEntity.ok(exists);
    }

    @PostMapping
    public ResponseEntity<DashboardCategoryDto> createCategory(@Valid @RequestBody DashboardCreateCategoryDto dashboardCreateCategoryDto) {
        Optional<DashboardCategoryDto> savedCategory = categoryService.createCategory(dashboardCreateCategoryDto);

        return savedCategory.map(category -> ResponseEntity.status(HttpStatus.CREATED).body(category))
                .orElseThrow(() -> new ResourceNotFoundException("Category could not be created"));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<DashboardCategoryDto> updateCategory(@PathVariable UUID id, @Valid @RequestBody DashboardUpdateCategoryDto dashboardUpdateCategoryDto) {
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
