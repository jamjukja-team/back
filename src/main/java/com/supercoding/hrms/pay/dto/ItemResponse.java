package com.supercoding.hrms.pay.dto;

//“급여 상세 항목” 데이터 (예: 기본급, 상여금, 4대보험 등)
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {
    private String itemCode;  // 항목 코드 (ex: SAL01)
    private String itemName;  // 항목 이름 (ex: 기본급)
    private int amount;       // 금액
    private String remark;    // 비고 (ex: 4대보험, 소득세 등)
}
