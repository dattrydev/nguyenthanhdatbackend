package com.nguyenthanhdat.blog.domain.dtos.dashboard.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponseDto {
    String url;
    String tempUrl;
}
