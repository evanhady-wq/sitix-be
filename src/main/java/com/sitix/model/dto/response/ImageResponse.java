package com.sitix.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ImageResponse {
    private String contentType;
    private String name;
    private Long size;
    private String path;
}