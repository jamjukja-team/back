package com.supercoding.hrms.leave.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_file")
@Builder
public class TblFile {

    @Id
    @Column(name = "file_id")
    private String fileId;

    @Column(name = "file_nm")
    private String fileNm;

    @Column(name = "file_type")
    private String fileType;

    // 애는 화면에서 바디로 보낼 때 빈갑으로 전달 할수 밖에 없음
    // 따라서 내가 따로 넣어줘야 함. 이 외에 pk들이 같은 이유로 프론트에서 바디로 보낼때 데이터를 빈값으로 보내
    @Column(name = "file_location")
    private String fileLocation;
}
