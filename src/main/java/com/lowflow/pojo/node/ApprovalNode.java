package com.lowflow.pojo.node;

import com.lowflow.pojo.enums.ApprovalMultiEnum;
import com.lowflow.pojo.enums.ApprovalNobodyEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.activiti.bpmn.model.*;

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
public class ApprovalNode extends AssigneeNode {
    // 表单属性
    private List<FormProperty> formProperties = new ArrayList<>();
    // 操作权限
    private Map<String, Boolean> operations = new LinkedHashMap<>();
    // 多人审批方式
    private ApprovalMultiEnum multi;
    // 审批人为空时处理方式
    private ApprovalNobodyEnum nobody;
    // 审批人为空时指定人员
    private List<String> nobodyUsers;

    @Override
    public List<FlowElement> convert() {
        ArrayList<FlowElement> elements = new ArrayList<>();
        // 用户节点
        UserTask userTask = new UserTask();
        userTask.setId(this.getId());
        userTask.setName(this.getName());
        // userTask.setAsynchronous(true);
        // userTask.setFormKey(this.getFormKey());
        // 监听器
        ArrayList<ActivitiListener> activitiListeners = new ArrayList<>();
        ActivitiListener createListener = new ActivitiListener();
        createListener.setEvent("create");
        createListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        createListener.setImplementation("${approvalCreatedListener}");
        activitiListeners.add(createListener);
        userTask.setTaskListeners(activitiListeners);
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
