package com.nguyenthanhdat.blog.controllers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardCreateTagDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardTagListDto;
import com.nguyenthanhdat.blog.domain.entities.Tag;
import com.nguyenthanhdat.blog.mappers.dashboard.DashboardTagMapper;
import com.nguyenthanhdat.blog.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/dashboard/v1/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final DashboardTagMapper dashboardTagMapper;

    @GetMapping
    public ResponseEntity<List<DashboardTagListDto>> getAllTags() {
        List<DashboardTagListDto> tags = tagService.listTags()
                .stream().map(dashboardTagMapper::toDto)
                .toList();

        return ResponseEntity.ok(tags);
    }

    @GetMapping(params = "name")
    public ResponseEntity<DashboardTagListDto> getTagByName(@RequestParam(name = "name") String name) {
        Optional<DashboardTagListDto> tag = tagService.getTagByName(name);
        return tag.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DashboardTagListDto> createTag(@Valid @RequestBody DashboardCreateTagDto dashboardCreateTagDto) {
        Tag tagToCreate = dashboardTagMapper.toEntity(dashboardCreateTagDto);
        Tag savedTag = tagService.createTag(tagToCreate);
        return new ResponseEntity<>(
                dashboardTagMapper.toDto(savedTag),
                HttpStatus.CREATED
        );
    }

    @PatchMapping
    public ResponseEntity<DashboardTagListDto> updateTag(@Valid @RequestBody Tag tag) {
        Tag updatedTag = tagService.updateTag(tag);
        return ResponseEntity.ok(dashboardTagMapper.toDto(updatedTag));
    }


    @DeleteMapping
    public ResponseEntity<Void> deleteTag(@RequestParam(name = "name") String name) {
        tagService.deleteTag(name);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
