package com.nguyenthanhdat.blog.controllers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardCreateTagDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardTagDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardTagListPagingDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardUpdateTagDto;
import com.nguyenthanhdat.blog.exceptions.ResourceCreationException;
import com.nguyenthanhdat.blog.exceptions.ResourceNotFoundException;
import com.nguyenthanhdat.blog.exceptions.ResourceUpdateException;
import com.nguyenthanhdat.blog.mappers.dashboard.DashboardTagMapper;
import com.nguyenthanhdat.blog.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard/v1/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<DashboardTagListPagingDto> getDashboardTagList(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        page = page - 1;
        Optional<DashboardTagListPagingDto> tags = tagService.getDashboardTagList(name, page, size, sortBy, sortDirection);

        return tags.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("No tags found"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DashboardTagDto> getDashboardTagById(@PathVariable UUID id){
        Optional<DashboardTagDto> tag = tagService.getDashboardTagById(id);
        return tag.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Tag " + id + " not found"));
    }

    @GetMapping("/check-unique")
    public ResponseEntity<Boolean> checkUniqueField(
            @RequestParam String field,
            @RequestParam String value) {
        boolean exists = tagService.isFieldExists(field, value);
        return ResponseEntity.ok(exists);
    }

    @PostMapping
    public ResponseEntity<DashboardTagDto> createTag(@Valid @RequestBody DashboardCreateTagDto dashboardCreateTagDto) {
        Optional<DashboardTagDto> savedTag = tagService.createTag(dashboardCreateTagDto);
        return savedTag.map(tag -> ResponseEntity.status(HttpStatus.CREATED).body(tag))
                .orElseThrow(() -> new ResourceCreationException("Tag could not be created"));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<DashboardTagDto> updateTag(@PathVariable UUID id, @Valid @RequestBody DashboardUpdateTagDto tag) {
        Optional<DashboardTagDto> updatedTag = tagService.updateTag(id, tag);
        return updatedTag.map(ResponseEntity::ok)
                .orElseThrow (() -> new ResourceUpdateException("Tag " + id + " could not be updated"));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
