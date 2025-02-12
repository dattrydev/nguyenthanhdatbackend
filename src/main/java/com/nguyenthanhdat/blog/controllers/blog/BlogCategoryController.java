package com.nguyenthanhdat.blog.controllers.blog;

import com.nguyenthanhdat.blog.domain.dtos.blog.category.BlogCategoryListDto;
import com.nguyenthanhdat.blog.exceptions.ResourceNotFoundException;
import com.nguyenthanhdat.blog.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/blog/v1/categories")
@RequiredArgsConstructor
public class BlogCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<BlogCategoryListDto> getBlogCategoryList() {
        Optional<BlogCategoryListDto> blogCategoryListDto = categoryService.getBlogCategoryList();

        return blogCategoryListDto.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("No categories found"));
    }
}
