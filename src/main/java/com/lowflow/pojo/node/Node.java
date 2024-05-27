package com.lowflow.pojo.node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.SequenceFlow;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @Title: Node
 * @Author：蔡晓峰
 * @Date：2023/11/26 14:16
 * @github：https://github.com/tsai996/lowflow-design
 * @gitee：https://gitee.com/cai_xiao_feng/lowflow-design
 * @description：节点
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", defaultImpl = Node.class, visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StartNode.class, name = "start"),
        @JsonSubTypes.Type(value = CcNode.class, name = "cc"),
        @JsonSubTypes.Type(value = ApprovalNode.class, name = "approval"),
        @JsonSubTypes.Type(value = ConditionNode.class, name = "condition"),
        @JsonSubTypes.Type(value = ExclusiveNode.class, name = "exclusive"),
        @JsonSubTypes.Type(value = TimerNode.class, name = "timer"),
        @JsonSubTypes.Type(value = NotifyNode.class, name = "notify"),
        @JsonSubTypes.Type(value = EndNode.class, name = "end")
})
public abstract class Node implements Serializable {
    private static final long serialVersionUID = 132324315232123L;
    // 节点id
    private String id;
    // 父节点id
    private String pid;
    // 节点名称
    private String name;
    // 节点类型
    @JsonTypeId
    private String type;
    // 子节点
    private Node child;
    // 分支id
    @JsonIgnore
    private String branchId;

    public abstract List<FlowElement> convert();

    public SequenceFlow buildSequence(Node next) {
        String sourceRef;
        String targetRef;
        if (Objects.nonNull(next)) {
            sourceRef = next.getPid();
            targetRef = next.getId();
        } else {
            if (StringUtils.isNotBlank(this.branchId)) {
                sourceRef = this.id;
                targetRef = this.branchId;
            } else {
                throw new RuntimeException(String.format("节点 %s 的下一个节点不能为空", this.id));
            }
        }
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setId(String.format("%s-%s", sourceRef, targetRef));
        sequenceFlow.setSourceRef(sourceRef);
        sequenceFlow.setTargetRef(targetRef);
        return sequenceFlow;
    }

}
