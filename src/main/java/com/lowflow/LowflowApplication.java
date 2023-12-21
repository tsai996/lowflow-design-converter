package com.lowflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Title: LowflowApplication
 * @Author：蔡晓峰
 * @Date：2023/11/26 14:16
 * @github：https://github.com/tsai996/lowflow-design
 * @gitee：https://gitee.com/cai_xiao_feng/lowflow-design
 * @description：启动类
 */
@SpringBootApplication(scanBasePackages = "com.lowflow")
public class LowflowApplication {
    public static void main(String[] args) {
        SpringApplication.run(LowflowApplication.class, args);
    }

}