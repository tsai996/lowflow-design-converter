package com.lowflow.pojo.node;

import com.lowflow.pojo.enums.NotifyTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.ImplementationType;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.ServiceTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotifyNode extends AssigneeNode {
    private List<NotifyTypeEnum> types;
    private String subject;
    private String content;

    @Override
    public List<FlowElement> convert() {
        ArrayList<FlowElement> elements = new ArrayList<>();
        // 服务节点
        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(this.getId());
        serviceTask.setName(this.getName());
        serviceTask.setAsynchronous(true);
        serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        serviceTask.setImplementation("${notifyDelegate}");
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
