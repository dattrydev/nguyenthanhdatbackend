package com.nguyenthanhdat.blog.exceptions.dashboard.category;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
