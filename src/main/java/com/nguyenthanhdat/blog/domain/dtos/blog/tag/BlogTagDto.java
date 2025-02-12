package com.nguyenthanhdat.blog.domain.dtos.blog.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogTagDto {
    private String name;
    private String slug;
    private Integer post_count;
}
