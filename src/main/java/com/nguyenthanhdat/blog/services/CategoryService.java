package com.nguyenthanhdat.blog.services;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.*;
import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    Optional<DashboardCategoryListPagingDto> getDashboardCategoryList(String name, int page, int size, String sortBy, String sortDirection);
    Optional<DashboardCategoryDto> getDashboardCategoryById(UUID id);
    Optional<DashboardCategoryDto> createCategory(DashboardCreateCategoryDto dashboardCreateCategoryDto);
    Optional<DashboardCategoryDto> updateCategory(UUID id, DashboardUpdateCategoryDto category);
    void deleteCategory(UUID id);
    boolean isFieldExists(String field, String value);
}
