package com.supercoding.hrms.pay.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item_nm")
@Getter @Setter
public class Item {
    @Id
    private String cd;

    private String nm;

    private String remark;
}
