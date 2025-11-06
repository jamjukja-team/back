package com.supercoding.hrms.pay.dto;

import com.supercoding.hrms.pay.domain.PayrollStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class PayrollCreateRequest {
    private Long empId;
    private String payMonth;
    private LocalDate payDate;
    private PayrollStatus status;
    private List<ItemCreateRequest> items;

    @Getter
    @NoArgsConstructor
    public static class ItemCreateRequest {
        private String itemCode;
        private int amount;
        private String remark;
    }
}
