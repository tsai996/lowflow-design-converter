package com.lowflow.pojo.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimerWaitType {
    DURATION("duration", "持续时间"),
    DATE("date", "日期");

    @JsonValue
    private final String type;
    private final String description;
}
