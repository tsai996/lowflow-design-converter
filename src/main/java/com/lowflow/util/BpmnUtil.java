package com.lowflow.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowflow.pojo.node.Node;
import com.lowflow.pojo.ProcessModel;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;

import java.util.List;


/**
 * @Title: BpmnUtil
 * @Author：蔡晓峰
 * @Date：2023/11/26 14:16
 * @github：https://github.com/tsai996/lowflow-design
 * @gitee：https://gitee.com/cai_xiao_feng/lowflow-design
 * @description：转换工具类
 */
@Slf4j
public class BpmnUtil {

    /**
     * 转为BpmnModel
     *
     * @param dto
     * @return
     */
    public static BpmnModel toBpmnModel(ProcessModel dto) {
        BpmnModel bpmnModel = new BpmnModel();
        // 命名空间
        bpmnModel.setTargetNamespace("http://activiti.org/bpmn20");
        // 创建一个流程
        Process process = new Process();
        // 设置流程的id
        process.setId(dto.getCode());
        // 设置流程的name
        process.setName(dto.getName());
        // 递归构建所有节点
        Node node = dto.getProcess();
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

    public static void main(String[] args) throws JsonProcessingException {
        String json = "{\n" +
                "    \"id\": \"root\",\n" +
                "    \"pid\": null,\n" +
                "    \"type\": \"start\",\n" +
                "    \"name\": \"发起人\",\n" +
                "    \"child\": {\n" +
                "        \"id\": \"end\",\n" +
                "        \"pid\": \"root\",\n" +
                "        \"type\": \"end\",\n" +
                "        \"name\": \"结束\",\n" +
                "        \"child\": null\n" +
                "    }\n" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        Node node = objectMapper.readValue(json, Node.class);
        ProcessModel processModelDTO = new ProcessModel();
        processModelDTO.setCode("test");
        processModelDTO.setName("测试");
        processModelDTO.setProcess(node);
        BpmnModel bpmnModel = toBpmnModel(processModelDTO);
        System.out.println("bpmnModel = " + bpmnModel);
    }

}
