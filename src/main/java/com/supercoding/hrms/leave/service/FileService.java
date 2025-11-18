package com.supercoding.hrms.leave.service;

import com.supercoding.hrms.leave.domain.TblFile;
import com.supercoding.hrms.leave.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    // File 정보 CRUD 구현

    private final FileRepository fileRepository;

    // 1. Create
    public TblFile create(MultipartFile file) {
        // 1) 서버 내부에 저장할 파일명(PK 기반으로 안전하게 생성)
        String fileId = UUID.randomUUID().toString();
        String originalName = file.getOriginalFilename();
        String extension = getFileExtension(originalName);

        // 2) 서버에 저장할 실제 경로(ex. /upload/uuid.pdf)
        String savedName = fileId + "." + extension;
        String savePath = "/upload/" + savedName; // 네 프로젝트 기준으로 수정 가능

        // 3) 디스크에 저장 (실제 저장 로직)
        saveToDisk(file, savePath);

        // 4) DB에 저장할 TblFile 생성
        TblFile tblFile = TblFile.builder()
                .fileId(fileId)
                .fileNm(originalName)
                .fileType(extension)
                .fileLocation(savePath)
                .build();

        return fileRepository.save(tblFile);
    }

    // 2. Read (단건)
    public TblFile read(String fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
    }

    // 3. Read (목록)
    public List<TblFile> readList() {
        return fileRepository.findAll();
    }

    // 5. Delete (단건)
    public void delete(String fileId) {
        fileRepository.deleteById(fileId);
    }

    // 6. Delete (다건)
    public void deleteList(List<String> ids) {
        ids.forEach(fileRepository::deleteById);
    }

    private String getFileExtension(String filename) {
        int index = filename.lastIndexOf(".");
        return filename.substring(index + 1);
    }

    private void saveToDisk(MultipartFile file, String path) {
        try {
            File target = new File(path);
            target.getParentFile().mkdirs();
            file.transferTo(target);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패");
        }
    }

}
