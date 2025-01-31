package com.nguyenthanhdat.blog.services;

import com.nguyenthanhdat.blog.domain.dtos.TagDto;

import java.util.List;
import java.util.Optional;

public interface TagService {
    List<TagDto> getAllTags();
    TagDto createTag(TagDto tag);
    Optional<TagDto> getTagByName(String name);
}
