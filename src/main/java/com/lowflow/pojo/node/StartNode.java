package com.lowflow.pojo.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;

import java.util.*;

/**
 * @Title: StartNode
 * @Author：蔡晓峰
 * @Date：2023/11/26 14:16
 * @github：https://github.com/tsai996/lowflow-design
 * @gitee：https://gitee.com/cai_xiao_feng/lowflow-design
 * @description：启动节点
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StartNode extends Node {
    // 表单属性
    private List<FormProperty> formProperties = new ArrayList<>();
    // 操作权限
    private Map<String, Boolean> operations = new LinkedHashMap<>();

    @Override
    public List<FlowElement> convert() {
        ArrayList<FlowElement> elements = new ArrayList<>();
        // 开始节点
        StartEvent startEvent = new StartEvent();
        startEvent.setId(this.getId());
        startEvent.setName(this.getName());
        elements.add(startEvent);
        // 下一个节点的连线
        Node child = this.getChild();
        SequenceFlow sequenceFlow = this.buildSequence(child);
        elements.add(sequenceFlow);
        // 下一个节点
        if (Objects.nonNull(child)) {
            List<FlowElement> flowElements = child.convert();
            elements.addAll(flowElements);
        }
        return elements;
    }
}
