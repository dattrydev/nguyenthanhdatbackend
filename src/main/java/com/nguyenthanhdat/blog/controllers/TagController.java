package com.nguyenthanhdat.blog.controllers;

import com.nguyenthanhdat.blog.domain.dtos.tag.CreateTagDto;
import com.nguyenthanhdat.blog.domain.dtos.tag.TagListDto;
import com.nguyenthanhdat.blog.domain.entities.Tag;
import com.nguyenthanhdat.blog.mappers.TagMapper;
import com.nguyenthanhdat.blog.services.TagService;
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
    public ResponseEntity<List<TagListDto>> getAllTags() {
        List<TagListDto> tags = tagService.listTags()
                .stream().map(tagMapper::toDto)
                .toList();

        return ResponseEntity.ok(tags);
    }

    @GetMapping(params = "name")
    public ResponseEntity<TagListDto> getTagByName(@RequestParam(name = "name") String name) {
        Optional<TagListDto> tag = tagService.getTagByName(name);
        return tag.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TagListDto> createTag(@Valid @RequestBody CreateTagDto createTagDto) {
        Tag tagToCreate = tagMapper.toEntity(createTagDto);
        Tag savedTag = tagService.createTag(tagToCreate);
        return new ResponseEntity<>(
                tagMapper.toDto(savedTag),
                HttpStatus.CREATED
        );
    }

    @PatchMapping
    public ResponseEntity<TagListDto> updateTag(@Valid @RequestBody Tag tag) {
        Tag updatedTag = tagService.updateTag(tag);
        return ResponseEntity.ok(tagMapper.toDto(updatedTag));
    }


    @DeleteMapping
    public ResponseEntity<Void> deleteTag(@RequestParam(name = "name") String name) {
        tagService.deleteTag(name);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
