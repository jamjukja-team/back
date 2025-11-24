package com.supercoding.hrms.com.service;

import com.supercoding.hrms.com.exception.CustomException;
import com.supercoding.hrms.com.exception.CustomMessage;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommonUploadService {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${file.upload-prefix}")
    private String prefix;

    public String uploadFile(MultipartFile file, String directory) {
        if (file == null || file.isEmpty()) {
            throw new CustomException(CustomMessage.FAIL_FILE_UPLOAD);
        }

        String ext = Optional.ofNullable(file.getOriginalFilename())
                .filter(name -> name.contains("."))
                .map(name -> name.substring(name.lastIndexOf(".")))
                .orElse("");

        //어느 도메인이든 디렉토리만 변경해서 사용 가능
        String key = "%s/%s%s".formatted(prefix, directory, UUID.randomUUID(), ext);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            return "https://%s.s3.%s.amazonaws.com/%s".formatted(bucket, region, key);
        } catch (Exception e) {
            throw new CustomException(CustomMessage.FAIL_FILE_UPLOAD);
        }

    }

    public String uploadFileTest(MultipartFile file, String prefixDir) throws FileUploadException {
        try {
            String fileName = prefix + "/" + prefixDir + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request,
                    RequestBody.fromBytes(file.getBytes()));

            return "https://" + bucket + ".s3.amazonaws.com/" + fileName;

        } catch (Exception e) {
            throw new FileUploadException("S3 업로드 실패: " + e.getMessage());
        }
    }
}
