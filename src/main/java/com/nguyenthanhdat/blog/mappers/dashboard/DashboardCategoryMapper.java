package com.nguyenthanhdat.blog.mappers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.DashboardCategoryDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.DashboardCategoryListDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.DashboardCreateCategoryDto;
import com.nguyenthanhdat.blog.domain.entities.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface DashboardCategoryMapper {
    DashboardCategoryListDto toCategoryListDto(Category category);

    DashboardCategoryDto toDto(Category category);

    Category toEntity(DashboardCreateCategoryDto dashboardCreateCategoryDto);

    DashboardCategoryListDto toPostListDto(Category category);
}
