package com.nguyenthanhdat.blog.mappers.blog;

import com.nguyenthanhdat.blog.domain.dtos.blog.category.BlogCategoryDto;
import com.nguyenthanhdat.blog.domain.projections.blog.category.BlogCategoryProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface BlogCategoryMapper {
        BlogCategoryDto toBlogCategoryDto(BlogCategoryProjection blogCategoryProjection);
}
