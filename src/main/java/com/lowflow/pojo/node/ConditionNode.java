package com.lowflow.pojo.node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lowflow.pojo.condition.FilterRules;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Title: ConditionNode
 * @Author：蔡晓峰
 * @Date：2023/11/26 14:16
 * @github：https://github.com/tsai996/lowflow-design
 * @gitee：https://gitee.com/cai_xiao_feng/lowflow-design
 * @description：条件(分支)
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConditionNode extends Node {
    private Boolean def;
    private FilterRules conditions;
    @JsonIgnore
    private Map<String, String> operatorMap = new HashMap<>();

    {
        // 等于
        operatorMap.put("eq", "var:eq(%s, %s)");
        // 不等于
        operatorMap.put("ne", "var:notEquals(%s, %s)");
        // 包含
        operatorMap.put("in", "var:containsAny(%s, %s)");
        // 不包含
        operatorMap.put("ni", "var:notContainsAny(%s, %s)");
        // 为空
        operatorMap.put("ul", "var:isNull(%s)");
        // 不为空
        operatorMap.put("nu", "var:isNotNull(%s)");
        // 字符包含
        operatorMap.put("lk", "var:contains(%s, %s)");
        // 大于
        operatorMap.put("gt", "var:gt(%s, %s)");
        // 小于
        operatorMap.put("lt", "var:lt(%s, %s)");
        // 小于或等于
        operatorMap.put("le", "var:lte(%s, %s)");
        // 大于或等于
        operatorMap.put("ge", "var:gte(%s, %s)");
    }

    protected String stringVal(Object val) {
        if (val instanceof String) {
            return String.format("'%s'", val);
        } else {
            return String.valueOf(val);
        }
    }

    public String toConditionExpression(FilterRules filterRules) {
        String expression = filterRules.getConditions().stream().map(e -> {
            String operator = operatorMap.get(e.getOperator());
            if (StringUtils.isNotBlank(operator)) {
                if (e.getValue() instanceof Collection) {
                    e.setValue(
                            ((Collection<?>) e.getValue())
                                    .stream()
                                    .map(this::stringVal)
                                    .collect(Collectors.joining(","))
                    );
                } else if (e.getValue() instanceof Object[]) {
                    e.setValue(
                            Arrays.stream((Object[]) e.getValue())
                                    .map(this::stringVal)
                                    .collect(Collectors.joining(","))
                    );
                } else if (e.getValue() instanceof String) {
                    e.setValue(String.format("'%s'", e.getValue()));
                }
                return String.format(operator,
                        e.getField(),
                        e.getValue()
                );
            } else {
                return "";
            }
        }).collect(Collectors.joining("and".equals(filterRules.getOperator()) ? " && " : " ||"));
        if (CollectionUtils.isEmpty(filterRules.getGroups())) {
            return expression;
        } else {
            String collect = filterRules
                    .getGroups()
                    .stream()
                    .map(this::toConditionExpression)
                    .collect(Collectors.joining("and".equals(filterRules.getOperator()) ? " && " : " || "));
            if (StringUtils.isNotBlank(expression)) {
                return String.format("(%s) %s (%s)", expression, "and".equals(filterRules.getOperator()) ? " && " : " || ", collect);
            } else {
                return collect;
            }
        }
    }

    @Override
    public List<FlowElement> convert() {
        ArrayList<FlowElement> elements = new ArrayList<>();
        // 条件节点
        SequenceFlow sequenceFlow = this.buildSequence(this);
        sequenceFlow.setId(this.getId());
        sequenceFlow.setName(this.getName());
        sequenceFlow.setTargetRef(
                Optional.ofNullable(this.getNext()).map(Node::getId).orElse(this.getBranchId())
        );
        String expression = this.toConditionExpression(this.getConditions());
        if (StringUtils.isNotBlank(expression)) {
            sequenceFlow.setConditionExpression(String.format("${%s}", expression));
        }
        elements.add(sequenceFlow);
        // 下一个节点
        Node child = this.getNext();
        if (Objects.nonNull(child)) {
            child.setBranchId(this.getBranchId());
            List<FlowElement> flowElements = child.convert();
            elements.addAll(flowElements);
        }
        return elements;
    }
}
