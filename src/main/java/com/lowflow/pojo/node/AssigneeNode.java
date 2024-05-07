package com.lowflow.pojo.node;

import com.lowflow.pojo.enums.AssigneeTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.activiti.bpmn.model.FlowElement;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AssigneeNode extends Node {
    // 审批对象
    private AssigneeTypeEnum assigneeType;
    // 表单内人员
    private String formUser;
    // 表单内角色
    private String formRole;
    // 审批人
    private List<String> users;
    // 审批人角色
    private List<String> roles;
    // 主管
    private Integer leader;
    // 组织主管
    private Integer orgLeader;
    // 发起人自选：true-单选，false-多选
    private Boolean choice;
    // 发起人自己
    private Boolean self;
    public abstract List<FlowElement> convert();
}
