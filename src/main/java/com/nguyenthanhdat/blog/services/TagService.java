package com.nguyenthanhdat.blog.services;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.*;

import java.util.Optional;
import java.util.UUID;

public interface TagService {
    Optional<DashboardTagListPagingDto> getDashboardTagList(String name, int page, int size, String sortBy, String sortDirection);
    Optional<DashboardTagDto> getDashboardTagById(UUID id);
    Optional<DashboardTagDto> createTag(DashboardCreateTagDto createTagDto);
    Optional<DashboardTagDto> updateTag(UUID id, DashboardUpdateTagDto tag);
    void deleteTag(UUID id);
}
