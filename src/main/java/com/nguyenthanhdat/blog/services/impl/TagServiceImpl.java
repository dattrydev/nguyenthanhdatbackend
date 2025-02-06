package com.nguyenthanhdat.blog.services.impl;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.*;
import com.nguyenthanhdat.blog.domain.entities.Tag;
import com.nguyenthanhdat.blog.exceptions.ResourceAlreadyExistsException;
import com.nguyenthanhdat.blog.exceptions.ResourceDeleteException;
import com.nguyenthanhdat.blog.exceptions.ResourceNotFoundException;
import com.nguyenthanhdat.blog.mappers.dashboard.DashboardTagMapper;
import com.nguyenthanhdat.blog.repositories.TagRepository;
import com.nguyenthanhdat.blog.services.TagService;
import com.nguyenthanhdat.blog.specification.TagSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final DashboardTagMapper dashboardTagMapper;

    @Override
    public Optional<DashboardTagListPagingDto> getDashboardTagList(String name, int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

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


    @Override
    public Optional<DashboardTagDto> getDashboardTagById(UUID id) {
        Optional<Tag> tag = tagRepository.findById(id);
        return tag.map(dashboardTagMapper::toDto);
    }

    @Override
    @Transactional
    public Optional<DashboardTagDto> createTag(DashboardCreateTagDto dashboardCreateTagDto) {
        if(tagRepository.existsByNameIgnoreCase(dashboardCreateTagDto.getName())) {
            throw new ResourceAlreadyExistsException("Tag with name " + dashboardCreateTagDto.getName() + " already exists");
        } else {
            Tag tagToCreate = dashboardTagMapper.toEntity(dashboardCreateTagDto);
            tagRepository.save(tagToCreate);
            return Optional.of(dashboardTagMapper.toDto(tagToCreate));
        }
    }

    @Override
    @Transactional
    public Optional<DashboardTagDto> updateTag(UUID id, DashboardUpdateTagDto dashboardUpdateTagDto) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag with id " + id + " not found"));

        if(tagRepository.existsByNameIgnoreCase(dashboardUpdateTagDto.getName())) {
            throw new ResourceAlreadyExistsException("Tag with name " + dashboardUpdateTagDto.getName() + " already exists");
        } else {
            tag.setName(dashboardUpdateTagDto.getName());
            tagRepository.save(tag);
            return Optional.of(dashboardTagMapper.toDto(tag));
        }
    }

    @Override
    public void deleteTag(UUID id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceDeleteException("Tag with id " + id + " not found"));

        try {
            tagRepository.delete(tag);
        } catch (Exception e) {
            throw new ResourceDeleteException("Failed to delete tag: " + id);
        }
    }

}
