package com.nguyenthanhdat.blog.exceptions.dashboard.post;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String message) {
        super(message);
    }
}
