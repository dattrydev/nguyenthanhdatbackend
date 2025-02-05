package com.nguyenthanhdat.blog.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationDto {
    private long totalRecords;
    private int totalPages;
    private int currentPage;
}
