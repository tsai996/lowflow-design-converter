package com.lowflow.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.lowflow.pojo.node.Node;
import lombok.Data;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;

import java.util.List;

/**
 * @Title: ProcessModel
 * @Author：蔡晓峰
 * @Date：2023/11/26 14:16
 * @github：https://github.com/tsai996/lowflow-design
 * @gitee：https://gitee.com/cai_xiao_feng/lowflow-design
 * @description：流程模型
 */
@Data
public class ProcessModel{
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String code;
    private String name;
    private Node process;
    private Integer version;
    private Integer sort;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;
    private String remark;

    public BpmnModel toBpmnModel() {
        BpmnModel bpmnModel = new BpmnModel();
        // 命名空间
        bpmnModel.setTargetNamespace("https://activiti.org/bpmn20");
        // 创建一个流程
        Process process = new Process();
        // 设置流程的id
        process.setId(this.getCode());
        // 设置流程的name
        process.setName(this.getName());
        // 设置流程的文档
        process.setDocumentation(this.getRemark());
        // 递归构建所有节点
        Node node = this.getProcess();
        List<FlowElement> flowElementList = node.convert();
        for (FlowElement flowElement : flowElementList) {
            process.addFlowElement(flowElement);
        }
        // 设置流程
        bpmnModel.addProcess(process);
        // 自动布局
        new BpmnAutoLayout(bpmnModel).execute();
        return bpmnModel;
    }

}
