package com.nguyenthanhdat.blog.mappers.blog;

import com.nguyenthanhdat.blog.domain.dtos.blog.tag.BlogTagDto;
import com.nguyenthanhdat.blog.domain.projections.blog.tag.BlogTagProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface BlogTagMapper {
    @Mapping(source = "postCount", target = "post_count")
    BlogTagDto toBlogTagDto(BlogTagProjection blogTagProjection);
}
