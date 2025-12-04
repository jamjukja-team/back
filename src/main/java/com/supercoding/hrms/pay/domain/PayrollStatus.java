package com.supercoding.hrms.pay.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum PayrollStatus {

    CALCULATING("계산중"),
    CONFIRMED("급여확정"),
    PAID("지급완료");

    private final String koreanName;

    PayrollStatus(String koreanName) {
        this.koreanName = koreanName;
    }

    /** ✅ 프론트 → 백 (영문 코드 → Enum) */
    @JsonCreator
    public static PayrollStatus from(String code) {
        for (PayrollStatus status : PayrollStatus.values()) {
            if (status.name().equalsIgnoreCase(code) || status.getKoreanName().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("잘못된 급여 상태 코드입니다: " + code);
    }

    /** ✅ 한글 이름 접근용 getter */
    public String getDisplayName() {
        return koreanName;
    }

    // 상태 메타데이터
    public static List<PayrollItem> toKeyValueList() {
        List<PayrollItem> list = new ArrayList<>();
        for (PayrollStatus status : PayrollStatus.values()) {
            PayrollItem payrollItem = new PayrollItem(status.name(), status.getDisplayName()); // name이 PAID고 getDisplayName이 "지급완료" 부분
            list.add(payrollItem);
        }
        return list;
    }
}

