package com.nguyenthanhdat.blog.mappers;

import com.nguyenthanhdat.blog.domain.dtos.tag.CreateTagDto;
import com.nguyenthanhdat.blog.domain.dtos.tag.TagListDto;
import com.nguyenthanhdat.blog.domain.entities.Tag;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface TagMapper {
    TagListDto toDto(Tag tag);

    Tag toEntity(CreateTagDto createTagDto);

    List<TagListDto> toDtoList(List<Tag> tags);
}
