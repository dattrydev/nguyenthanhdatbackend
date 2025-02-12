package com.nguyenthanhdat.blog.services.impl;

import com.nguyenthanhdat.blog.domain.entities.User;
import com.nguyenthanhdat.blog.domain.repositories.UserRepository;
import com.nguyenthanhdat.blog.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public User findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }
}
