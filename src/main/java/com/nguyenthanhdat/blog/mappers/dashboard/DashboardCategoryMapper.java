package com.nguyenthanhdat.blog.mappers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.DashboardCategoryListDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.category.DashboardCreateCategoryDto;
import com.nguyenthanhdat.blog.domain.enums.PostStatus;
import com.nguyenthanhdat.blog.domain.entities.Category;
import com.nguyenthanhdat.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface DashboardCategoryMapper {

    @Mapping(target="postCount", source = "posts", qualifiedByName = "caculatePostCount")
    DashboardCategoryListDto toDto(Category category);

    Category toEntity(DashboardCreateCategoryDto dashboardCreateCategoryDto);

    @Named("caculatePostCount")
    default long caculatePostCount(List<Post> posts){
        if(null == posts){
            return 0;
        }

        return posts.stream().filter(post -> PostStatus.PUBLISHED.equals(post.getStatus())).count();
    }
}
