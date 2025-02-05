package com.nguyenthanhdat.blog.controllers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardCreateTagDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardTagDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardTagListPagingDto;
import com.nguyenthanhdat.blog.domain.entities.Tag;
import com.nguyenthanhdat.blog.exceptions.ResourceNotFoundException;
import com.nguyenthanhdat.blog.mappers.dashboard.DashboardTagMapper;
import com.nguyenthanhdat.blog.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/dashboard/v1/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final DashboardTagMapper dashboardTagMapper;

    @GetMapping
    public ResponseEntity<DashboardTagListPagingDto> getDashboardTagList(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Optional<DashboardTagListPagingDto> tags = tagService.getDashboardTagList(name, page, size);

        return tags.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("No tags found"));
    }

    @GetMapping(params = "name")
    public ResponseEntity<DashboardTagDto> getTagByName(@RequestParam(name = "name") String name) {
        Optional<DashboardTagDto> tag = tagService.getTagByName(name);
        return tag.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Tag " + name + " not found"));
    }

    @PostMapping
    public ResponseEntity<DashboardTagDto> createTag(@Valid @RequestBody DashboardCreateTagDto dashboardCreateTagDto) {
        Tag tagToCreate = dashboardTagMapper.toEntity(dashboardCreateTagDto);
        Optional<Tag> savedTag = tagService.createTag(tagToCreate);
        return savedTag.map(tag -> ResponseEntity.status(HttpStatus.CREATED).body(dashboardTagMapper.toDto(tag)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()

        );
    }

    @PatchMapping
    public ResponseEntity<DashboardTagDto> updateTag(@Valid @RequestBody Tag tag) {
        Optional<Tag> updatedTag = tagService.updateTag(tag);
        return updatedTag.map(t -> ResponseEntity.ok(dashboardTagMapper.toDto(t)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTag(@RequestParam(name = "name") String name) {
        tagService.deleteTag(name);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
