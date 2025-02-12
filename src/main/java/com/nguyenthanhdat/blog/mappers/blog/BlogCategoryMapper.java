package com.nguyenthanhdat.blog.mappers.blog;

import com.nguyenthanhdat.blog.domain.dtos.blog.category.BlogCategoryDto;
import com.nguyenthanhdat.blog.domain.projections.blog.category.BlogCategoryProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface BlogCategoryMapper {
        @Mapping(source = "postCount", target = "post_count")
        BlogCategoryDto toBlogCategoryDto(BlogCategoryProjection blogCategoryProjection);
}
