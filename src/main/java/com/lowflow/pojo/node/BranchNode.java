package com.lowflow.pojo.node;

import lombok.Data;
import org.flowable.bpmn.model.FlowElement;

import java.util.List;

/**
 * @Title: BranchNode
 * @Author：蔡晓峰
 * @Date：2023/11/26 14:16
 * @github：https://github.com/tsai996/lowflow-design
 * @gitee：https://gitee.com/cai_xiao_feng/lowflow-design
 * @description：分支节点
 */
@Data
public abstract class BranchNode extends Node {
    private List<ConditionNode> branches;

    public abstract List<FlowElement> convert();

}
