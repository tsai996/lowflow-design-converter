package com.lowflow.pojo.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowElement;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @Title: ExclusiveNode
 * @Author：蔡晓峰
 * @Date：2023/11/26 14:16
 * @github：https://github.com/tsai996/lowflow-design
 * @gitee：https://gitee.com/cai_xiao_feng/lowflow-design
 * @description：独占分支
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExclusiveNode extends BranchNode {


    @Override
    public List<FlowElement> convert() {
        ArrayList<FlowElement> elements = new ArrayList<>();
        // 独占分支
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(this.getId());
        exclusiveGateway.setName(this.getName());
        elements.add(exclusiveGateway);
        List<ConditionNode> branches = this.getBranches();
        branches.stream()
                .filter(ConditionNode::getDef)
                .findFirst()
                .ifPresent(conditionNode -> {
                    exclusiveGateway.setDefaultFlow(conditionNode.getId());
                });
        // 子节点
        if (!CollectionUtils.isEmpty(branches)) {
            for (Node next : branches) {
                String branchId = Optional.ofNullable(this.getNext()).map(Node::getId).orElse(this.getBranchId());
                next.setBranchId(branchId);
                elements.addAll(next.convert());
            }
        }
        // 下一个节点
        Node next = this.getNext();
        if (Objects.nonNull(next)) {
            next.setBranchId(this.getBranchId());
            List<FlowElement> flowElements = next.convert();
            elements.addAll(flowElements);
        }
        return elements;
    }
}
