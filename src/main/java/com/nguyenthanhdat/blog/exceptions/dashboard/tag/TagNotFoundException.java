package com.nguyenthanhdat.blog.exceptions.dashboard.tag;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException(String message) {
        super(message);
    }
}
