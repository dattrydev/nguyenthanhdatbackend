package com.nguyenthanhdat.blog.mappers;

import com.nguyenthanhdat.blog.domain.dtos.TagDto;
import com.nguyenthanhdat.blog.domain.entities.Tag;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface TagMapper {
    TagDto toDto(Tag tag);

    Tag toEntity(TagDto tagDto);

    List<TagDto> toDtoList(List<Tag> tags);
}
