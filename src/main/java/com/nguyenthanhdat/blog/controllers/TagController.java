package com.nguyenthanhdat.blog.controllers;

import com.nguyenthanhdat.blog.domain.dtos.TagDto;
import com.nguyenthanhdat.blog.services.impl.TagServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagServiceImpl tagServiceImpl;

    @GetMapping
    public List<TagDto> getAllTags() {
        return tagServiceImpl.getAllTags();
    }

    @GetMapping(params = "name")
    public ResponseEntity<TagDto> getTagByName(@RequestParam(name = "name") String name) {
        Optional<TagDto> tag = tagServiceImpl.getTagByName(name);
        return tag.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public TagDto createTag(@RequestBody TagDto tag) {
        return tagServiceImpl.createTag(tag);
    }
}
