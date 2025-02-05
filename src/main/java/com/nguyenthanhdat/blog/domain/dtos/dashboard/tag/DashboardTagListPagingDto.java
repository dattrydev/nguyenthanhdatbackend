package com.nguyenthanhdat.blog.domain.dtos.dashboard.tag;

import com.nguyenthanhdat.blog.domain.dtos.PaginationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DashboardTagListPagingDto extends PaginationDto {
    private List<DashboardTagListDto> tags;
}
