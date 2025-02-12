package com.nguyenthanhdat.blog.mappers.blog;

import com.nguyenthanhdat.blog.domain.dtos.blog.post.BlogPostDto;
import com.nguyenthanhdat.blog.domain.dtos.blog.post.BlogPostListDto;
import com.nguyenthanhdat.blog.domain.entities.Post;
import com.nguyenthanhdat.blog.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface BlogPostMapper {
    BlogPostListDto toBlogPostListDto(Post post);

    BlogPostDto toBlogPostDto(Post post);
}
