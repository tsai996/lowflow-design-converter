package com.lowflow.pojo.node;

import lombok.Data;

/**
 * @Title: FormProperty
 * @Author：蔡晓峰
 * @Date：2023/11/26 14:16
 * @github：https://github.com/tsai996/lowflow-design
 * @gitee：https://gitee.com/cai_xiao_feng/lowflow-design
 * @description：表单字段属性
 */
@Data
public class FormProperty {
    private String id;
    private String name;
    private boolean readable = true;
    private boolean writeable = true;
    private boolean hidden;
    private boolean required;
}
