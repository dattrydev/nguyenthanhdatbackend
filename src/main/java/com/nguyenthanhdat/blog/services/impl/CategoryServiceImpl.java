package com.nguyenthanhdat.blog.services.impl;

import com.nguyenthanhdat.blog.domain.dtos.blog.category.BlogCategoryDto;
import com.nguyenthanhdat.blog.domain.dtos.blog.category.BlogCategoryListDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.*;
import com.nguyenthanhdat.blog.domain.entities.Category;
import com.nguyenthanhdat.blog.domain.projections.blog.category.BlogCategoryProjection;
import com.nguyenthanhdat.blog.exceptions.ResourceAlreadyExistsException;
import com.nguyenthanhdat.blog.exceptions.ResourceDeleteException;
import com.nguyenthanhdat.blog.exceptions.ResourceNotFoundException;
import com.nguyenthanhdat.blog.mappers.blog.BlogCategoryMapper;
import com.nguyenthanhdat.blog.mappers.dashboard.DashboardCategoryMapper;
import com.nguyenthanhdat.blog.domain.repositories.CategoryRepository;
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

import static com.nguyenthanhdat.blog.utils.SlugGenerator.generateSlug;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final DashboardCategoryMapper dashboardCategoryMapper;
    private final BlogCategoryMapper blogCategoryMapper;

    @Override
    public Optional<DashboardCategoryListPagingDto> getDashboardCategoryList(String name, int page, int size, String sortBy, String sortDirection) {
        if (page < 0) {
            throw new IllegalArgumentException("Page number must be greater than 0");
        }

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Specification<Category> specification = Specification.where(CategorySpecification.hasName(name));

        Page<Category> categoryPage = categoryRepository.findAll(specification, pageable);


        List<DashboardCategoryListDto> categoryListDtos = categoryPage.stream()
                .map(dashboardCategoryMapper::toCategoryListDto)
                .toList();

        int totalPages = categoryPage.getTotalPages();

        DashboardCategoryListPagingDto dashboardCategoryListPagingDto = DashboardCategoryListPagingDto.builder()
                .categories(categoryListDtos)
                .totalPages(totalPages)
                .currentPage(page + 1)
                .build();

        return Optional.of(dashboardCategoryListPagingDto);
    }

    @Override
    public Optional<DashboardCategoryDto> getDashboardCategoryBySlug(String slug) {
        Optional<Category> category = categoryRepository.findBySlug(slug);

        return Optional.of(dashboardCategoryMapper.toDto(category.orElseThrow(() -> new ResourceNotFoundException("Category with slug " + slug + " not found"))));
    }

    @Override
    @Transactional
    public Optional<DashboardCategoryDto> createCategory(DashboardCreateCategoryDto dashboardCreateCategoryDto) {
        if(categoryRepository.existsByNameIgnoreCase(dashboardCreateCategoryDto.getName())){
            throw new ResourceAlreadyExistsException("Category with name " + dashboardCreateCategoryDto.getName() + " already exists");
        } else {
            String generatedSlug = generateSlug(dashboardCreateCategoryDto.getName());

            Category categoryToCreate = Category.builder()
                    .name(dashboardCreateCategoryDto.getName())
                    .slug(generatedSlug)
                    .build();

            categoryRepository.save(categoryToCreate);

            UUID id = categoryToCreate.getId();

            return Optional.of(dashboardCategoryMapper.toDto(categoryToCreate)).map(category -> {
                category.setId(id);
                return category;
            });
        }
    }

    @Override
    @Transactional
    public Optional<DashboardCategoryDto> updateCategory(UUID id, DashboardUpdateCategoryDto dashboardUpdateCategoryDto) {
        Optional<Category> existingCategory = categoryRepository.findById(id);

        if(existingCategory.isPresent()){
            Category updatedCategory = existingCategory.get();

            if (categoryRepository.existsByNameIgnoreCase(dashboardUpdateCategoryDto.getName()) && !updatedCategory.getName().equalsIgnoreCase(dashboardUpdateCategoryDto.getName())) {
                throw new ResourceAlreadyExistsException("Category with name " + dashboardUpdateCategoryDto.getName() + " already exists");
            }
            updatedCategory.setName(dashboardUpdateCategoryDto.getName());
            updatedCategory.setSlug(generateSlug(dashboardUpdateCategoryDto.getName()));
            categoryRepository.save(updatedCategory);

            return Optional.of(dashboardCategoryMapper.toDto(updatedCategory));
        } else {
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteCategories(List<UUID> ids) {
        for (UUID id : ids) {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Category " + id + " not found"));

            try {
                categoryRepository.delete(category);
            } catch (DataIntegrityViolationException e) {
                throw new ResourceDeleteException("Cannot delete category with id " + id + " because it is referenced in another entity.");
            } catch (Exception e) {
                throw new ResourceDeleteException("Failed to delete category: " + id + ". Error: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean isFieldExists(String field, String value) {
        return switch (field) {
            case "name" -> categoryRepository.existsByName(value);
            default -> false;
        };
    }

    @Override
    public Optional<BlogCategoryListDto> getBlogCategoryList() {
        List<BlogCategoryProjection> categories = categoryRepository.findAllCategoriesWithPostCount();

        List<BlogCategoryDto> blogCategoryListDtos = categories.stream()
                .map(blogCategoryMapper::toBlogCategoryDto)
                .toList();

        BlogCategoryListDto blogCategoryListDto = BlogCategoryListDto.builder()
                .categories(blogCategoryListDtos)
                .build();

        return Optional.of(blogCategoryListDto);
    }

}
