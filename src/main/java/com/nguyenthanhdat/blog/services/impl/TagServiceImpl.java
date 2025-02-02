package com.nguyenthanhdat.blog.services.impl;

import com.nguyenthanhdat.blog.domain.dtos.tag.TagListDto;
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

    @Override
    public List<Tag> listTags() {
        return tagRepository.findAll();
    }

    public Optional<TagListDto> getTagByName(String name) {
        Optional<Tag> tag = tagRepository.findByName(name);
        return tag.map(tagMapper::toDto);
    }

    @Override
    public Tag createTag(Tag tag) {
        if(tagRepository.existsByNameIgnoreCase(tag.getName())) {
            throw new IllegalArgumentException("Tag with name " + tag.getName() + " already exists");
        } else {
            return tagRepository.save(tag);
        }
    }

    @Override
    public Tag updateTag(Tag tag) {
        Optional<Tag> existingTag = tagRepository.findById(tag.getId());
        if(existingTag.isPresent()) {
            Tag updatedTag = existingTag.get();
            updatedTag.setName(tag.getName());
            return tagRepository.save(updatedTag);
        } else {
            throw new IllegalArgumentException("Tag with name " + tag.getName() + " not found");
        }
    }

    @Override
    public void deleteTag(String name) {
        Optional<Tag> tag = tagRepository.findByName(name);
        if(tag.isPresent()) {
            tagRepository.delete(tag.get());
        } else {
            throw new IllegalArgumentException("Tag with name " + name + " not found");
        }
    }
}
