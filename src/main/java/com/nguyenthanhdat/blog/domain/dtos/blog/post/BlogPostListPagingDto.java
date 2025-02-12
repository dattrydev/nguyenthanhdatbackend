package com.nguyenthanhdat.blog.domain.dtos.blog.post;

import com.nguyenthanhdat.blog.domain.dtos.PaginationDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BlogPostListPagingDto extends PaginationDto{
    List<BlogPostListDto> posts;
}
