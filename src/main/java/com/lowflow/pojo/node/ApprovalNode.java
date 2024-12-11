package com.lowflow.pojo.node;

import com.lowflow.pojo.enums.ApprovalMultiEnum;
import com.lowflow.pojo.enums.ApprovalNobodyEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.activiti.bpmn.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

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
    // 多人会签通过百分比
    private BigDecimal multiPercent;
    // 审批人为空时处理方式
    private ApprovalNobodyEnum nobody;
    // 审批人为空时指定人员
    private List<String> nobodyUsers;
    // 任务监听器
    private List<NodeListener> taskListeners;

    @Override
    public List<FlowElement> convert() {
        ArrayList<FlowElement> elements = new ArrayList<>();
        // 用户节点
        UserTask userTask = new UserTask();
        userTask.setId(this.getId());
        userTask.setName(this.getName());
        // userTask.setAsynchronous(true);
        // userTask.setFormKey(this.getFormKey());
        userTask.setExecutionListeners(this.buidEventListener());
        if (!CollectionUtils.isEmpty(this.taskListeners)) {
            List<ActivitiListener> listeners = this.taskListeners.stream().filter(l -> StringUtils.isNotBlank(l.getImplementation())).map(listener -> {
                ActivitiListener eventListener = new ActivitiListener();
                eventListener.setEvent(listener.getEvent());
                eventListener.setImplementation(listener.getImplementation());
                eventListener.setImplementationType(listener.getImplementationType());
                return eventListener;
            }).collect(Collectors.toList());
            userTask.setTaskListeners(listeners);
        }
        // 审批人
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = new MultiInstanceLoopCharacteristics();
        if (this.getMulti() == ApprovalMultiEnum.SEQUENTIAL) {
            multiInstanceLoopCharacteristics.setSequential(true);
        } else if (this.getMulti() == ApprovalMultiEnum.JOINT) {
            multiInstanceLoopCharacteristics.setSequential(false);
            if (Objects.nonNull(this.getMultiPercent()) && this.getMultiPercent().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal percent = this.getMultiPercent().divide(new BigDecimal(100), 2, RoundingMode.DOWN);
                multiInstanceLoopCharacteristics.setCompletionCondition(String.format("${nrOfCompletedInstances/nrOfInstances >= %s}", percent));
            }
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
        Node next = this.getNext();
        SequenceFlow sequenceFlow = this.buildSequence(next);
        elements.add(sequenceFlow);
        // 下一个节点
        if (Objects.nonNull(next)) {
            next.setBranchId(this.getBranchId());
            List<FlowElement> flowElements = next.convert();
            elements.addAll(flowElements);
        }
        return elements;
    }

}
