package com.nguyenthanhdat.blog.mappers;

import com.nguyenthanhdat.blog.domain.dtos.tag.CreateTagRequest;
import com.nguyenthanhdat.blog.domain.dtos.tag.TagDto;
import com.nguyenthanhdat.blog.domain.entities.Tag;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface TagMapper {
    TagDto toDto(Tag tag);

    Tag toEntity(CreateTagRequest createTagRequest);

    List<TagDto> toDtoList(List<Tag> tags);
}
