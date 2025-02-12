package com.nguyenthanhdat.blog.controllers.blog;

import com.nguyenthanhdat.blog.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blog/v1/categories")
@RequiredArgsConstructor
public class BlogCategoryController {
    private final CategoryService categoryService;


}
