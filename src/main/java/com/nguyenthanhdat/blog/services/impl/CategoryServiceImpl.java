package com.nguyenthanhdat.blog.services.impl;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.*;
import com.nguyenthanhdat.blog.domain.entities.Category;
import com.nguyenthanhdat.blog.exceptions.ResourceAlreadyExistsException;
import com.nguyenthanhdat.blog.exceptions.ResourceDeleteException;
import com.nguyenthanhdat.blog.exceptions.ResourceNotFoundException;
import com.nguyenthanhdat.blog.mappers.dashboard.DashboardCategoryMapper;
import com.nguyenthanhdat.blog.repositories.CategoryRepository;
import com.nguyenthanhdat.blog.services.CategoryService;
import com.nguyenthanhdat.blog.specification.CategorySpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final DashboardCategoryMapper dashboardCategoryMapper;

    @Override
    public Optional<DashboardCategoryListPagingDto> getDashboardCategoryList(String name, int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Specification<Category> specification = Specification.where(CategorySpecification.hasName(name));

        Page<Category> categoryPage = categoryRepository.findAll(specification, pageable);

        List<DashboardCategoryListDto> categoryListDtos = categoryPage.stream()
                .map(dashboardCategoryMapper::toCategoryListDto)
                .toList();

        long totalRecords = categoryRepository.count(specification);
        int totalPages = categoryPage.getTotalPages();

        DashboardCategoryListPagingDto dashboardCategoryListPagingDto = DashboardCategoryListPagingDto.builder()
                .categories(categoryListDtos)
                .totalRecords(totalRecords)
                .totalPages(totalPages)
                .build();

        return Optional.of(dashboardCategoryListPagingDto);
    }

    @Override
    public Optional<DashboardCategoryDto> getDashboardCategoryById(UUID id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isPresent()){
            return Optional.of(dashboardCategoryMapper.toDto(category.get()));
        } else {
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }
    }

    @Override
    @Transactional
    public Optional<DashboardCategoryDto> createCategory(DashboardCreateCategoryDto dashboardCreateCategoryDto) {
        if(categoryRepository.existsByNameIgnoreCase(dashboardCreateCategoryDto.getName())){
            throw new ResourceAlreadyExistsException("Category with name " + dashboardCreateCategoryDto.getName() + " already exists");
        } else {
            Category categoryToCreate = dashboardCategoryMapper.toEntity(dashboardCreateCategoryDto);
            categoryRepository.save(categoryToCreate);

            return Optional.of(dashboardCategoryMapper.toDto(categoryToCreate));
        }
    }

    @Override
    @Transactional
    public Optional<DashboardCategoryDto> updateCategory(UUID id, DashboardUpdateCategoryDto dashboardUpdateCategoryDto) {
        Optional<Category> existingCategory = categoryRepository.findById(id);

        if(existingCategory.isPresent()){
            Category updatedCategory = existingCategory.get();
            updatedCategory.setName(dashboardUpdateCategoryDto.getName());
            categoryRepository.save(updatedCategory);

            return Optional.of(dashboardCategoryMapper.toDto(updatedCategory));
        } else {
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }
    }

    @Override
    public void deleteCategory(UUID id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));

        try {
            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceDeleteException("Cannot delete category with id " + id + " because it is referenced in another entity.");
        } catch (Exception e) {
            throw new ResourceDeleteException("Failed to delete category: " + id + ". Error: " + e.getMessage());
        }
    }

    @Override
    public boolean isFieldExists(String field, String value) {
        return switch (field) {
            case "name" -> categoryRepository.existsByName(value);
            default -> false;
        };
    }

}
