package com.nguyenthanhdat.blog.exceptions.dashboard.post;

public class PostAlreadyExistsException extends RuntimeException {
    public PostAlreadyExistsException(String message) {
        super(message);
    }
}
