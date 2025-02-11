package com.nguyenthanhdat.blog.services;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    Optional<DashboardCategoryListPagingDto> getDashboardCategoryList(String name, int page, int size, String sortBy, String sortDirection);
    Optional<DashboardCategoryDto> getDashboardCategoryBySlug(String slug);
    Optional<DashboardCategoryDto> createCategory(DashboardCreateCategoryDto dashboardCreateCategoryDto);
    Optional<DashboardCategoryDto> updateCategory(UUID id, DashboardUpdateCategoryDto category);
    void deleteCategory(UUID id);
    void deleteCategories(List<UUID> ids);
    boolean isFieldExists(String field, String value);
}
