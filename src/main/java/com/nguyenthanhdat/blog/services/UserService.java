package com.nguyenthanhdat.blog.services;

import com.nguyenthanhdat.blog.domain.entities.User;

public interface UserService {
    public User findUserByEmail(String email);
}
