package com.nguyenthanhdat.blog.mappers;

import com.nguyenthanhdat.blog.domain.dtos.post.PostListDto;
import com.nguyenthanhdat.blog.domain.entities.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface PostMapper {
    PostListDto toDto(Post post);
}
