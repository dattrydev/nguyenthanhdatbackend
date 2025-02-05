package com.nguyenthanhdat.blog.domain.dtos.dashboard.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardTagListDto {
    private UUID id;
    private String name;
}
