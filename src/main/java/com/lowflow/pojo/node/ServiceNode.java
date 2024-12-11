package com.lowflow.pojo.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.ServiceTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceNode extends Node {
    private String implementationType;
    private String implementation;

    @Override
    public List<FlowElement> convert() {
        ArrayList<FlowElement> elements = new ArrayList<>();
        // 服务节点
        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(this.getId());
        serviceTask.setName(this.getName());
        serviceTask.setExecutionListeners(this.buidEventListener());
        serviceTask.setImplementationType(implementationType);
        serviceTask.setImplementation(implementation);
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
