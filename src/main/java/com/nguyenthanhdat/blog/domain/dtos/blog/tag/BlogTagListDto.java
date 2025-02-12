package com.nguyenthanhdat.blog.domain.dtos.blog.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogTagListDto {
    private String name;
    private String slug;
    private Integer postCount;
}
