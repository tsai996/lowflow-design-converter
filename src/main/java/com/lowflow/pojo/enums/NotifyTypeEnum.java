package com.lowflow.pojo.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotifyTypeEnum {
    SITE("site", "站内"),
    EMAIL("email", "邮件"),
    SMS("sms", "短信"),
    WECHAT("wechat", "微信"),
    DINGTALK("dingtalk", "钉钉"),
    FEISHU("feishu", "飞书");

    @JsonValue
    private final String type;
    private final String description;
}
