package com.lowflow.pojo.node;

import com.lowflow.pojo.enums.ApprovalMultiEnum;
import com.lowflow.pojo.enums.ApprovalNobodyEnum;
import com.lowflow.pojo.enums.ApprovalTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.flowable.bpmn.model.FormProperty;
import org.flowable.bpmn.model.*;

import java.util.*;

/**
 * @Title: ApprovalNode
 * @Author：蔡晓峰
 * @Date：2023/11/26 14:16
 * @github：https://github.com/tsai996/lowflow-design
 * @gitee：https://gitee.com/cai_xiao_feng/lowflow-design
 * @description：审批节点
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApprovalNode extends Node {
    // 表单属性
    private List<FormProperty> formProperties = new ArrayList<>();
    // 操作权限
    private Map<String, Boolean> operations = new LinkedHashMap<>();
    // 审批方式
    private ApprovalTypeEnum assigneeType;
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
    // 发起人自选：true-单选，false-多选
    private Boolean choice;
    // 发起人自己
    private Boolean self;
    // 多人审批方式
    private ApprovalMultiEnum multi;
    // 审批人为空时处理方式
    private ApprovalNobodyEnum nobody;

    @Override
    public List<FlowElement> convert() {
        ArrayList<FlowElement> elements = new ArrayList<>();
        // 用户节点
        UserTask userTask = new UserTask();
        userTask.setId(this.getId());
        userTask.setName(this.getName());
        userTask.setAsynchronous(true);
        // userTask.setFormKey(this.getFormKey());
        // 监听器
        ArrayList<FlowableListener> flowableListeners = new ArrayList<>();
        FlowableListener createListener = new FlowableListener();
        createListener.setEvent("create");
        createListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        createListener.setImplementation("${approvalCreatedListener}");
        flowableListeners.add(createListener);
        userTask.setTaskListeners(flowableListeners);
        // 审批人
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = new MultiInstanceLoopCharacteristics();
        if (this.getMulti() == ApprovalMultiEnum.SEQUENTIAL) {
            multiInstanceLoopCharacteristics.setSequential(true);
        } else if (this.getMulti() == ApprovalMultiEnum.JOINT) {
            multiInstanceLoopCharacteristics.setSequential(false);
        } else if (this.getMulti() == ApprovalMultiEnum.SINGLE) {
            multiInstanceLoopCharacteristics.setSequential(false);
            multiInstanceLoopCharacteristics.setCompletionCondition("${nrOfCompletedInstances > 0}");
        }
        String variable = String.format("%sItem", this.getId());
        multiInstanceLoopCharacteristics.setElementVariable(variable);
        multiInstanceLoopCharacteristics.setInputDataItem(String.format("${%sCollection}", this.getId()));
        userTask.setLoopCharacteristics(multiInstanceLoopCharacteristics);
        userTask.setAssignee(String.format("${%s}", variable));
        elements.add(userTask);
        // 下一个节点的连线
        Node child = this.getChild();
        SequenceFlow sequenceFlow = this.buildSequence(child);
        elements.add(sequenceFlow);
        // 下一个节点
        if (Objects.nonNull(child)) {
            child.setBranchId(this.getBranchId());
            List<FlowElement> flowElements = child.convert();
            elements.addAll(flowElements);
        }
        return elements;
    }

}
