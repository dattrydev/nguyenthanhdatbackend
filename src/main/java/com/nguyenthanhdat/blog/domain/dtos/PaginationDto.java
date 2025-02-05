package com.nguyenthanhdat.blog.domain.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@RequiredArgsConstructor
@SuperBuilder
public class PaginationDto {
    protected long totalRecords;
    protected int totalPages;
    protected int currentPage;
}