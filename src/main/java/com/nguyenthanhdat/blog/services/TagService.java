package com.nguyenthanhdat.blog.services;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardTagListDto;
import com.nguyenthanhdat.blog.domain.entities.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    List<Tag> listTags();
    Optional<DashboardTagListDto> getTagByName(String name);
    Tag createTag(Tag tag);
    Tag updateTag(Tag tag);
    void deleteTag(String name);
}
