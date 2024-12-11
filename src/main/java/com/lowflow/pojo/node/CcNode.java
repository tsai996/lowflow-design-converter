package com.lowflow.pojo.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.ServiceTask;

import java.util.*;

/**
 * @Title: CcNode
 * @Author：蔡晓峰
 * @Date：2023/11/26 14:16
 * @github：https://github.com/tsai996/lowflow-design
 * @gitee：https://gitee.com/cai_xiao_feng/lowflow-design
 * @description：抄送节点
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CcNode extends AssigneeNode {
    // 表单属性
    private List<FormProperty> formProperties = new ArrayList<>();
    // 操作权限
    private Map<String, Boolean> operations = new LinkedHashMap<>();

    @Override
    public List<FlowElement> convert() {
        ArrayList<FlowElement> elements = new ArrayList<>();
        // 服务节点
        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(this.getId());
        serviceTask.setName(this.getName());
        // serviceTask.setAsynchronous(true);
        serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        serviceTask.setImplementation("${ccDelegate}");
        elements.add(serviceTask);
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
