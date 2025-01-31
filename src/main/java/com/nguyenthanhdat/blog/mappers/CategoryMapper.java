package com.nguyenthanhdat.blog.mappers;

import com.nguyenthanhdat.blog.domain.PostStatus;
import com.nguyenthanhdat.blog.domain.dtos.category.CategoryDto;
import com.nguyenthanhdat.blog.domain.dtos.category.CreateCategoryRequest;
import com.nguyenthanhdat.blog.domain.entities.Category;
import com.nguyenthanhdat.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target="postCount", source = "posts", qualifiedByName = "caculatePostCount")
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequest createCategoryRequest);

    @Named("caculatePostCount")
    default long caculatePostCount(List<Post> posts){
        if(null == posts){
            return 0;
        }

        return posts.stream().filter(post -> PostStatus.PUBLISHED.equals(post.getStatus())).count();
    }
}
