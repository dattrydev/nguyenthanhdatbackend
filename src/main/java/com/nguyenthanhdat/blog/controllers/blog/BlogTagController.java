package com.nguyenthanhdat.blog.controllers.blog;

import com.nguyenthanhdat.blog.domain.dtos.blog.tag.BlogTagListDto;
import com.nguyenthanhdat.blog.exceptions.ResourceNotFoundException;
import com.nguyenthanhdat.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blog/v1/tags")
@RequiredArgsConstructor
public class BlogTagController {
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<BlogTagListDto>> getBlogTagList() {
        Optional<List<BlogTagListDto>> blogTagListDto = tagService.getBlogTagList();

        return blogTagListDto.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("No tags found"));
    }
}
