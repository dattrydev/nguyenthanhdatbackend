package com.nguyenthanhdat.blog.services;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostListPagingDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardTagDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardTagListDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardTagListPagingDto;
import com.nguyenthanhdat.blog.domain.entities.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    Optional<DashboardTagListPagingDto> getDashboardTagList(String name, int page, int size);
    Optional<DashboardTagDto> getTagByName(String name);
    Optional<Tag> createTag(Tag tag);
    Optional<Tag> updateTag(Tag tag);
    void deleteTag(String name);
}
