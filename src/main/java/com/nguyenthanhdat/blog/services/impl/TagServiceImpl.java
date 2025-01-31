package com.nguyenthanhdat.blog.services.impl;

import com.nguyenthanhdat.blog.domain.dtos.TagDto;
import com.nguyenthanhdat.blog.domain.entities.Tag;
import com.nguyenthanhdat.blog.mappers.TagMapper;
import com.nguyenthanhdat.blog.repositories.TagRepository;
import com.nguyenthanhdat.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public List<TagDto> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tagMapper.toDtoList(tags);
    }

    public Optional<TagDto> getTagByName(String name) {
        Optional<Tag> tag = tagRepository.findByName(name);
        return tag.map(tagMapper::toDto);
    }

    public TagDto createTag(TagDto tagDto) {
        Tag tag = tagMapper.toEntity(tagDto);
        tag = tagRepository.save(tag);
        return tagMapper.toDto(tag);
    }
}
