package com.lowflow.pojo.node;

import com.lowflow.pojo.enums.TimerWaitType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class TimerNode extends Node {
    private TimerWaitType waitType;
    private String unit;
    private Integer duration;
    private String timeDate;

    @Override
    public List<FlowElement> convert() {
        ArrayList<FlowElement> elements = new ArrayList<>();
        // 计时器节点
        IntermediateCatchEvent intermediateCatchEvent = new IntermediateCatchEvent();
        intermediateCatchEvent.setId(this.getId());
        intermediateCatchEvent.setName(this.getName());
        ArrayList<EventDefinition> eventDefinitions = new ArrayList<>();;
        TimerEventDefinition timerEventDefinition = new TimerEventDefinition();
        if (waitType == TimerWaitType.DURATION) {
            timerEventDefinition.setTimeDuration(String.format(this.getUnit(), this.getDuration()));
        } else if (waitType == TimerWaitType.DATE) {
            String time = this.getTimeDate();
            if (StringUtils.isNotBlank(time)) {
                time = time.replace(" ", "T");
                timerEventDefinition.setTimeDate(time);
            }
        }
        eventDefinitions.add(timerEventDefinition);
        intermediateCatchEvent.setEventDefinitions(eventDefinitions);
        elements.add(intermediateCatchEvent);
        // 下一个节点的连线
        Node child = this.getChild();
        SequenceFlow sequenceFlow = this.buildSequence(child);
        elements.add(sequenceFlow);
        // 下一个节点
        if (Objects.nonNull(child)) {
            child.setBranchId(this.getBranchId());
            List<FlowElement> flowElements = child.convert();
            elements.addAll(flowElements);
        }
        return elements;
    }
}
