package com.nguyenthanhdat.blog.mappers.dashboard;

import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardCreateTagDto;
import com.nguyenthanhdat.blog.domain.dtos.dashboard.tag.DashboardTagListDto;
import com.nguyenthanhdat.blog.domain.entities.Tag;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface DashboardTagMapper {
    DashboardTagListDto toDto(Tag tag);

    Tag toEntity(DashboardCreateTagDto dashboardCreateTagDto);
}
