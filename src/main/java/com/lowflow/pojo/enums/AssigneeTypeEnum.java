package com.lowflow.pojo.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssigneeTypeEnum {
    USER("user", "用户"),
    ROLE("role", "角色"),
    CHOICE("choice", "发起人自选"),
    SELF("self", "发起人自己"),
    LEADER("leader", "主管"),
    ORG_LEADER("orgLeader", "组织主管"),
    FORM_USER("formUser", "表单用户"),
    FORM_ROLE("formRole", "表单角色"),
    AUTO_REFUSE("autoRefuse", "自动拒绝"),
    AUTO_PASS("autoPass", "自动通过");

    @JsonValue
    private final String type;
    private final String description;
}
