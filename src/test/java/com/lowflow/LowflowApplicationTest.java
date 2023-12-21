package com.lowflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowflow.pojo.ProcessModel;
import com.lowflow.pojo.node.Node;
import com.lowflow.util.BpmnUtil;
import org.flowable.bpmn.model.BpmnModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Title: LowflowTest
 * @Author 蔡晓峰
 * @Date 2023/11/26 14:23
 * @github：https://github.com/tsai996/lowflow-design
 * @gitee：https://gitee.com/cai_xiao_feng/lowflow-design
 */
@SpringBootTest(classes = LowflowApplication.class)
@RunWith(SpringRunner.class)
public class LowflowApplicationTest {

    @Test
    public void test() throws JsonProcessingException {
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
        BpmnModel bpmnModel = BpmnUtil.toBpmnModel(processModelDTO);
        System.out.println("bpmnModel = " + bpmnModel);
    }
}
