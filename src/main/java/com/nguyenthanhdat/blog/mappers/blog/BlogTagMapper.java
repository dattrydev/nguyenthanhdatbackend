package com.nguyenthanhdat.blog.mappers.blog;

import com.nguyenthanhdat.blog.domain.dtos.blog.tag.BlogTagDto;
import com.nguyenthanhdat.blog.domain.projections.blog.tag.BlogTagProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface BlogTagMapper {
    BlogTagDto toBlogTagDto(BlogTagProjection blogTagProjection);
}
