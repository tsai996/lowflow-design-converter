package com.lowflow.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.lowflow.pojo.node.Node;
import lombok.Data;

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

}
