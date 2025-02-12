package com.nguyenthanhdat.blog.services.impl;

import com.nguyenthanhdat.blog.domain.dtos.blog.tag.BlogTagDto;
import com.nguyenthanhdat.blog.domain.dtos.blog.tag.BlogTagListDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.*;
import com.nguyenthanhdat.blog.domain.entities.Tag;
import com.nguyenthanhdat.blog.domain.projections.blog.tag.BlogTagProjection;
import com.nguyenthanhdat.blog.exceptions.ResourceAlreadyExistsException;
import com.nguyenthanhdat.blog.exceptions.ResourceDeleteException;
import com.nguyenthanhdat.blog.exceptions.ResourceNotFoundException;
import com.nguyenthanhdat.blog.mappers.blog.BlogTagMapper;
import com.nguyenthanhdat.blog.mappers.dashboard.DashboardTagMapper;
import com.nguyenthanhdat.blog.domain.repositories.TagRepository;
import com.nguyenthanhdat.blog.services.TagService;
import com.nguyenthanhdat.blog.specification.TagSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.nguyenthanhdat.blog.utils.SlugGenerator.generateSlug;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final DashboardTagMapper dashboardTagMapper;
    private final BlogTagMapper blogTagMapper;

    @Override
    public Optional<DashboardTagListPagingDto> getDashboardTagList(String name, int page, int size, String sortBy, String sortDirection) {
        if (page < 0) {
            throw new IllegalArgumentException("Page number must be greater than 0");
        }

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Specification<Tag> specification = Specification.where(TagSpecification.hasName(name));

        Page<Tag> tagPage = tagRepository.findAll(specification, pageable);

        List<DashboardTagListDto> tagListDtos = tagPage.stream()
                .map(dashboardTagMapper::toTagListDto)
                .toList();

        int totalPages = tagPage.getTotalPages();

        DashboardTagListPagingDto dashboardTagListPagingDto = DashboardTagListPagingDto.builder()
                .tags(tagListDtos)
                .totalPages(totalPages)
                .currentPage(page + 1)
                .build();

        return Optional.of(dashboardTagListPagingDto);
    }

    @Override
    public Optional<DashboardTagDto> getDashboardTagBySlug(String slug) {
        Optional<Tag> tag = tagRepository.findBySlug(slug);

        return Optional.of(dashboardTagMapper.toDto(tag.orElseThrow(() -> new ResourceNotFoundException("Tag with slug " + slug + " not found"))));
    }

    @Override
    @Transactional
    public Optional<DashboardTagDto> createTag(DashboardCreateTagDto dashboardCreateTagDto) {
        if(tagRepository.existsByNameIgnoreCase(dashboardCreateTagDto.getName())) {
            throw new ResourceAlreadyExistsException("Tag with name " + dashboardCreateTagDto.getName() + " already exists");
        } else {
            String generatedSlug = generateSlug(dashboardCreateTagDto.getName());
            Tag tagToCreate = Tag.builder()
                    .name(dashboardCreateTagDto.getName())
                    .slug(generatedSlug)
                    .build();
            tagRepository.save(tagToCreate);

            UUID id = tagToCreate.getId();

            return Optional.of(dashboardTagMapper.toDto(tagToCreate)).map(tag -> {
                tag.setId(id);
                return tag;
            });
        }
    }

    @Override
    @Transactional
    public Optional<DashboardTagDto> updateTag(UUID id, DashboardUpdateTagDto dashboardUpdateTagDto) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag with id " + id + " not found"));

        if(tagRepository.existsByNameIgnoreCase(dashboardUpdateTagDto.getName() ) && !tag.getName().equalsIgnoreCase(dashboardUpdateTagDto.getName())) {
            throw new ResourceAlreadyExistsException("Tag with name " + dashboardUpdateTagDto.getName() + " already exists");
        } else {
            tag.setName(dashboardUpdateTagDto.getName());
            tag.setSlug(generateSlug(dashboardUpdateTagDto.getName()));
            tagRepository.save(tag);
            return Optional.of(dashboardTagMapper.toDto(tag));
        }
    }

    @Override
    @Transactional
    public void deleteTag(UUID id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceDeleteException("Tag with id " + id + " not found"));

        try {
            tagRepository.delete(tag);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceDeleteException("Cannot delete tag with id " + id + " because it is referenced in another entity.");
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceDeleteException("Cannot delete tag with id " + id + " because it does not exist.");
        } catch (Exception e) {
            throw new ResourceDeleteException("Failed to delete tag: " + id + ". Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteTags(List<UUID> ids) {
        for (UUID id : ids) {
            Tag tag = tagRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Tag " + id + " not found"));

            try {
                tagRepository.delete(tag);
            } catch (DataIntegrityViolationException e) {
                throw new ResourceDeleteException("Cannot delete tag with id " + id + " because it is referenced in another entity.");
            } catch (EmptyResultDataAccessException e) {
                throw new ResourceDeleteException("Cannot delete tag with id " + id + " because it does not exist.");
            } catch (Exception e) {
                throw new ResourceDeleteException("Failed to delete tag: " + id + ". Error: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean isFieldExists(String field, String value) {
        return switch (field) {
            case "name" -> tagRepository.existsByName(value);
            default -> false;
        };
    }

    @Override
    public Optional<BlogTagListDto> getBlogTagList() {
        List<BlogTagProjection> blogTagProjections = tagRepository.findAllTagsWithPostCount();

        List<BlogTagDto> blogTagDtos = blogTagProjections.stream()
                .map(blogTagMapper::toBlogTagDto)
                .toList();

        BlogTagListDto blogTagListDto = BlogTagListDto.builder()
                .tags(blogTagDtos)
                .build();

        return Optional.of(blogTagListDto);
    }


}
