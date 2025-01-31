package com.nguyenthanhdat.blog.domain.dtos;

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
public class TagDto {
    private String name;
    private Set<Post> posts;
}
