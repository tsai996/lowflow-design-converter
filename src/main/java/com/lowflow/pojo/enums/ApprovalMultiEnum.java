package com.lowflow.pojo.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ApprovalMultiEnum {
    SEQUENTIAL("sequential", "多人审批方式-顺序审批"),
    JOINT("joint", "多人审批方式-并行审批"),
    SINGLE("single", "多人审批方式-任何人审批");

    @JsonValue
    private final String multi;
    private final String description;

    ApprovalMultiEnum(String method, String description) {
        this.multi = method;
        this.description = description;
    }

}
