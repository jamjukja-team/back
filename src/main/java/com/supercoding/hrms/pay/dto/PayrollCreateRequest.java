package com.supercoding.hrms.pay.dto;

import com.supercoding.hrms.pay.domain.PayrollStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

//애는 Payroll안에 들어감
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PayrollCreateRequest {
    private Long empId;
    private String payMonth;
    private String status;
    private List<ItemCreateRequest> items;

    //애는 PayrollDetail 안에 들어감
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemCreateRequest {
        private String itemCd;     // ✅ DB/엔티티 필드와 일치
        private String itemNm;     // ✅ 항목 이름 (선택)
        private int amount;        // ✅ 금액
        private String remark;     // ✅ note → remark로 변경
    }
}
