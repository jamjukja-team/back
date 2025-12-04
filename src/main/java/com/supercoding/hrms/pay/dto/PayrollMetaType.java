package com.supercoding.hrms.pay.dto;

import com.supercoding.hrms.pay.domain.PayrollItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayrollMetaType {
    private List<PayrollItem> statusList;
    private List<PayrollItem> itemList;
}
