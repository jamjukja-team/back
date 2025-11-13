package com.supercoding.hrms.pay.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

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
}