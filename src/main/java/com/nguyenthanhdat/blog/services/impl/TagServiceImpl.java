package com.nguyenthanhdat.blog.services.impl;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostListDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.post.DashboardPostListPagingDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardTagDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardTagListDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardTagListPagingDto;
import com.nguyenthanhdat.blog.domain.entities.Tag;
import com.nguyenthanhdat.blog.exceptions.ResourceAlreadyExistsException;
import com.nguyenthanhdat.blog.exceptions.ResourceNotFoundException;
import com.nguyenthanhdat.blog.mappers.dashboard.DashboardTagMapper;
import com.nguyenthanhdat.blog.repositories.TagRepository;
import com.nguyenthanhdat.blog.services.TagService;
import com.nguyenthanhdat.blog.specification.TagSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final DashboardTagMapper dashboardTagMapper;

    @Override
    public Optional<DashboardTagListPagingDto> getDashboardTagList(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Tag> specification = Specification.where(TagSpecification.hasName(name));

        Page<Tag> tagPage = tagRepository.findAll(specification, pageable);

        List<DashboardTagListDto> tagListDtos = tagPage.stream()
                .map(dashboardTagMapper::toTagListDto)
                .toList();

        long totalRecords = tagRepository.count(specification);
        int totalPages = tagPage.getTotalPages();

        DashboardTagListPagingDto dashboardTagListPagingDto = DashboardTagListPagingDto.builder()
                .tags(tagListDtos)
                .totalRecords(totalRecords)
                .totalPages(totalPages)
                .currentPage(page)
                .build();

        return Optional.of(dashboardTagListPagingDto);
    }

    public Optional<DashboardTagDto> getTagByName(String name) {
        Optional<Tag> tag = tagRepository.findByName(name);
        return tag.map(dashboardTagMapper::toDto);
    }

    @Override
    public Optional<Tag> createTag(Tag tag) {
        if(tagRepository.existsByNameIgnoreCase(tag.getName())) {
            throw new ResourceAlreadyExistsException("Tag with name " + tag.getName() + " already exists");
        } else {
            return Optional.of(tagRepository.save(tag));
        }
    }

    @Override
    public Optional<Tag> updateTag(Tag tag) {
        Optional<Tag> existingTag = tagRepository.findById(tag.getId());
        if(existingTag.isPresent()) {
            Tag updatedTag = existingTag.get();
            updatedTag.setName(tag.getName());
            return Optional.of(tagRepository.save(updatedTag));
        } else {
            throw new ResourceNotFoundException("Tag with name " + tag.getName() + " not found");
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
