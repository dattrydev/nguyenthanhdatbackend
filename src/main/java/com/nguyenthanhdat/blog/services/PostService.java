package com.nguyenthanhdat.blog.services;

import com.nguyenthanhdat.blog.domain.entities.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    public List<Post> getAllPosts();
    public Optional<Post> getPostBySlug(String slug);
    public Post createPost(Post post);
}
