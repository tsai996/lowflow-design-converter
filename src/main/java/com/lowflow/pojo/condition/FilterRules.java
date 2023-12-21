package com.lowflow.pojo.condition;

import lombok.Data;

import java.util.List;

/**
 * 筛选规则
 */
@Data
public class FilterRules {
    private String logicalOperator;
    private List<Condition> conditions;
    private List<FilterRules> groups;
}
