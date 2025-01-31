package com.nguyenthanhdat.blog.controllers;

import com.nguyenthanhdat.blog.domain.dtos.tag.CreateTagRequest;
import com.nguyenthanhdat.blog.domain.dtos.tag.TagDto;
import com.nguyenthanhdat.blog.domain.entities.Tag;
import com.nguyenthanhdat.blog.mappers.TagMapper;
import com.nguyenthanhdat.blog.services.TagService;
import com.nguyenthanhdat.blog.services.impl.TagServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
        List<TagDto> tags = tagService.listTags()
                .stream().map(tagMapper::toDto)
                .toList();

        return ResponseEntity.ok(tags);
    }

    @GetMapping(params = "name")
    public ResponseEntity<TagDto> getTagByName(@RequestParam(name = "name") String name) {
        Optional<TagDto> tag = tagService.getTagByName(name);
        return tag.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TagDto> createTag(@Valid @RequestBody CreateTagRequest createTagRequest) {
        Tag tagToCreate = tagMapper.toEntity(createTagRequest);
        Tag savedTag = tagService.createTag(tagToCreate);
        return new ResponseEntity<>(
                tagMapper.toDto(savedTag),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTag(@RequestParam(name = "name") String name) {
        tagService.deleteTag(name);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
