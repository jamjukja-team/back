package com.supercoding.hrms.pay.dto;

//“급여 상세 항목” 데이터 (예: 기본급, 상여금, 4대보험 등)
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {
    private String itemCode;   // ✅ 항목 코드 (예: BASIC, MEAL, TAX)
    private String itemName;   // 항목명 (예: 기본급, 식대)
    private Integer amount;    // 금액
    private String remark;     // 비고
}
