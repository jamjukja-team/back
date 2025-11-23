package com.supercoding.hrms.leave.controller;

import com.supercoding.hrms.leave.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<?> create(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(fileService.create(file));
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<?> read(@PathVariable String fileId) {
        return ResponseEntity.ok(fileService.read(fileId));
    }

    @GetMapping
    public ResponseEntity<?> readList() {
        return ResponseEntity.ok(fileService.readList());
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> delete(@PathVariable String fileId) {
        fileService.delete(fileId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteList(@RequestBody List<String> fileIds) {
        fileService.deleteList(fileIds);
        return ResponseEntity.ok().build();
    }
}
