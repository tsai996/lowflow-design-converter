package com.lowflow.pojo.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.flowable.bpmn.model.FlowElement;

import java.util.List;

/**
 * @Title: ParallelNode
 * @Author：蔡晓峰
 * @Date：2023/11/26 14:16
 * @github：https://github.com/tsai996/lowflow-design
 * @gitee：https://gitee.com/cai_xiao_feng/lowflow-design
 * @description：并行节点
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ParallelNode extends BranchNode {
    private String name;

    @Override
    public List<FlowElement> convert() {
        // 待添加
        return null;
    }
}
