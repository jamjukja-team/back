package com.supercoding.hrms.leave.service;

import com.supercoding.hrms.com.service.CommonUploadService;
import com.supercoding.hrms.leave.domain.TblFile;
import com.supercoding.hrms.leave.domain.TblLeave;
import com.supercoding.hrms.leave.dto.LeaveType;
import com.supercoding.hrms.leave.repository.FileRepository;
import com.supercoding.hrms.leave.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveService {
    // Leave 정보 CRUD 구현

    private final LeaveRepository leaveRepository;
    private final FileRepository fileRepository;
    private final CommonUploadService uploadService;

    // 1. Create
    public LeaveType create(TblLeave leave, MultipartFile file) {
        if(file != null){
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            String ext = fileName.substring(fileName.lastIndexOf(".")+1);
            // upload 파일 안의 key는 leave라는 키로 설정된 폴더 안에 파일 저장함
            // 반환하는건 서버에 저장된 파일의 location 임.
            String location = uploadService.uploadFile(file, "leave");
            String fileId = random16();
            TblFile tblFile = new TblFile(
                    fileId,
                    fileName,
                    ext,
                    location
            );
            TblFile saveFile = fileRepository.save(tblFile);

            leave.setFileId(fileId);
            TblLeave saveLeave = leaveRepository.save(leave);

            return new LeaveType(saveLeave, saveFile);
        }else{
            TblLeave saveLeave = leaveRepository.save(leave);
            return new LeaveType(saveLeave, null);
        }
    }

    // 2. Read (단건)
    public LeaveType read(Long leaveId) {
        TblLeave tblLeave = leaveRepository.findById(leaveId).orElse(null);
        String fileId = tblLeave!=null?tblLeave.getFileId():"";
        TblFile tblFile = fileId.isEmpty()?null:fileRepository.findById(fileId).orElse(null);

        return new LeaveType(tblLeave, tblFile);
    }

    // 3. Read (목록)
    public List<LeaveType> readList() {
        List<TblLeave> lists = leaveRepository.findAll();

        return lists.stream().map(list -> read(list.getLeaveId())).toList();
    }

    // 5. Delete (단건)
    public boolean delete(Long leaveId) {
        try {
            leaveRepository.deleteById(leaveId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 6. Delete (다건)
    public boolean deleteList(List<Long> leaveIds) {
        try {
            leaveIds.forEach(leaveRepository::deleteById);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 승인 반려 처리
    public void updateStatus(Long leaveId, String reason, String status) {
        LeaveType leaveType = read(leaveId);
        leaveType.getLeaveInfo().setLeaveStatus(status);
        leaveType.getLeaveInfo().setRejectReason(reason);
        leaveRepository.save(leaveType.getLeaveInfo());
    }

    public String random16() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(16);

        for (int i = 0; i < 16; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }

}
