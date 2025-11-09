package com.supercoding.hrms.pay.dto;

import com.supercoding.hrms.pay.domain.PayrollStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

//애는 Payroll안에 들어감
@Getter
@NoArgsConstructor
public class PayrollCreateRequest {
    private Long empId;
    private String payMonth;
    private String status;
    private List<ItemCreateRequest> items;

    //애는 PayrollDetail 안에 들어감
    @Getter
    @NoArgsConstructor
    public static class ItemCreateRequest {
        private String itemCode;
        private int amount;
        private String note;
    }
}
