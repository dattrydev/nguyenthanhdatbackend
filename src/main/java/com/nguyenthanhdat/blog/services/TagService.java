package com.nguyenthanhdat.blog.services;

import com.nguyenthanhdat.blog.domain.dtos.tag.TagDto;
import com.nguyenthanhdat.blog.domain.entities.Tag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TagService {
    List<Tag> listTags();
    Optional<TagDto> getTagByName(String name);
    Tag createTag(Tag tag);
    Tag updateTag(Tag tag);
    void deleteTag(String name);
}
