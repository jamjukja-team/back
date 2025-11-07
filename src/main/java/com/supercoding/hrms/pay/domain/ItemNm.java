package com.supercoding.hrms.pay.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "item_nm")
public class ItemNm {

    @Id
    private String cd; // 항목이름의 id : BASIC", "MEAL", "TAX"

    private String nm;
    private String field;

}
