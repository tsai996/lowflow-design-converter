package com.lowflow.pojo.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ApprovalTypeEnum {
    USER("user", "用户"),
    ROLE("role", "角色"),
    CHOICE("choice", "发起人自选"),
    SELF("self", "发起人自己"),
    LEADER("leader", "主管"),
    FORM_USER("formUser", "表单用户"),
    FORM_ROLE("formRole", "表单角色");

    @JsonValue
    private final String type;
    private final String description;

    ApprovalTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }


}
