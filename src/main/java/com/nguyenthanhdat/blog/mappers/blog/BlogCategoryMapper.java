package com.nguyenthanhdat.blog.mappers.blog;

import com.nguyenthanhdat.blog.domain.dtos.blog.category.BlogCategoryListDto;
import com.nguyenthanhdat.blog.domain.projections.blog.category.BlogCategoryListProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface BlogCategoryMapper {
        BlogCategoryListDto toBlogCategoryListDto(BlogCategoryListProjection blogCategoryListProjection);
}
