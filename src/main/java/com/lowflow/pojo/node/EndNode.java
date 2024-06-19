package com.lowflow.pojo.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: EndNode
 * @Author：蔡晓峰
 * @Date：2023/11/26 14:16
 * @github：https://github.com/tsai996/lowflow-design
 * @gitee：https://gitee.com/cai_xiao_feng/lowflow-design
 * @description：结束节点
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EndNode extends Node {

    @Override
    public List<FlowElement> convert() {
        ArrayList<FlowElement> elements = new ArrayList<>();
        // 结束节点
        EndEvent endEvent = new EndEvent();
        endEvent.setId(this.getId());
        endEvent.setName(this.getName());
        endEvent.setExecutionListeners(this.buidEventListener());
        elements.add(endEvent);
        return elements;
    }
}
