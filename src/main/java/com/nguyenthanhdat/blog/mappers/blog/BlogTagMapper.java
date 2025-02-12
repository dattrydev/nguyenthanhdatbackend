package com.nguyenthanhdat.blog.mappers.blog;

import com.nguyenthanhdat.blog.domain.dtos.blog.tag.BlogTagListDto;
import com.nguyenthanhdat.blog.domain.projections.blog.tag.BlogTagListProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface BlogTagMapper {
    BlogTagListDto toBlogTagListDto(BlogTagListProjection blogTagListProjection);
}
