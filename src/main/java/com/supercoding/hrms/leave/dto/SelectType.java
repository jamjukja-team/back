package com.supercoding.hrms.leave.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectType {
    // 메타데이터, 드롭박스의 데이터들을 넘겨주기 위한 용도
    // 프론트 라이브러리에서 cd와 nm 을 넘겨주면 좋아함

    private String cd;
    private String nm;
}
