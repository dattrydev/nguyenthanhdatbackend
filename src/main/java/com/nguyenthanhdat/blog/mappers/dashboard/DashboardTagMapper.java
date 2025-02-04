package com.nguyenthanhdat.blog.mappers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.CreateTagDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.TagListDto;
import com.nguyenthanhdat.blog.domain.entities.Tag;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface DashboardTagMapper {
    TagListDto toDto(Tag tag);

    Tag toEntity(CreateTagDto createTagDto);

    List<TagListDto> toDtoList(List<Tag> tags);
}
