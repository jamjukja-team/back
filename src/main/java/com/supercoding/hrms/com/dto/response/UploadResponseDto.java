package com.supercoding.hrms.com.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadResponseDto {
    private String fileName;
    private String url;
}
