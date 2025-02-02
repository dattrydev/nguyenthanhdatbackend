package com.nguyenthanhdat.blog.domain.dtos.tag;

import com.nguyenthanhdat.blog.domain.entities.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagListDto {
    private String name;
    private Set<Post> posts;
}
