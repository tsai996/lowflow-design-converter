package com.lowflow.pojo.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ApprovalNobodyEnum {
    REJECT("reject", "驳回"),
    PASS("pass", "通过");

    @JsonValue
    private final String nobody;
    private final String description;

    ApprovalNobodyEnum(String nobody, String description) {
        this.nobody = nobody;
        this.description = description;
    }


}
