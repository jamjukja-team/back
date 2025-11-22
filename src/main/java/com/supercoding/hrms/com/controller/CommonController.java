package com.supercoding.hrms.com.controller;

import com.supercoding.hrms.com.dto.response.CommonMetaDataResponseDto;
import com.supercoding.hrms.com.service.CommonMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/com/metadata")
@RequiredArgsConstructor
public class CommonController {
    private final CommonMetadataService commonMetadataService;

    @GetMapping
    public CommonMetaDataResponseDto getMetaData() {
        return commonMetadataService.getMetaData();
    }
}
