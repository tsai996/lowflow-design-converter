<div align="center">
  <h1>lowflow-design-converter</h1>
  <p>将低代码流程定义转换为 Flowable 兼容的 BPMN 2.0 XML。</p>

  <p>
    <a href="README.md">English</a> · <strong>简体中文</strong>
  </p>

  <p>
    <img alt="Java 8" src="https://img.shields.io/badge/Java-8-orange" />
    <img alt="Spring Boot 2.3.12" src="https://img.shields.io/badge/Spring%20Boot-2.3.12-6DB33F?logo=springboot&logoColor=white" />
    <img alt="Flowable 6.8.0" src="https://img.shields.io/badge/Flowable-6.8.0-007ACC" />
    <img alt="GPL-3.0 许可证" src="https://img.shields.io/badge/License-GPL--3.0-blue" />
    <img alt="GitHub stars" src="https://img.shields.io/github/stars/tsai996/lowflow-design-converter?style=flat&logo=github" />
  </p>
</div>

## 项目简介

`lowflow-design-converter` 是 [lowflow-design](https://github.com/tsai996/lowflow-design) 可视化流程设计器的后端转换工具。它接收 JSON 流程树，构建 Flowable BPMN 模型、自动完成流程图布局，并返回可下载的 `.bpmn20.xml` 文件。

- JSON 转 BPMN 2.0 XML
- 使用 Flowable 自动布局流程图
- 通过 REST 接口直接下载 XML
- `main` 分支适配 Flowable，`activiti` 分支适配 Activiti

## 快速开始

环境要求：JDK 8、Maven 3.6+。

```bash
git clone https://github.com/tsai996/lowflow-design-converter.git
cd lowflow-design-converter
mvn org.springframework.boot:spring-boot-maven-plugin:2.3.12.RELEASE:run
```

也可以在 IDE 中直接运行 `com.lowflow.LowflowApplication`。服务默认启动在 `http://localhost:8080/lowflow`。

## API

### 下载 BPMN XML

```text
POST /lowflow/model/download
Content-Type: application/json
```

新建最小流程文件 `process.json`：

```json
{
  "code": "leave-process",
  "name": "请假流程",
  "remark": "最小的开始到结束流程",
  "process": {
    "id": "start",
    "type": "start",
    "name": "开始",
    "next": {
      "id": "end",
      "pid": "start",
      "type": "end",
      "name": "结束"
    }
  }
}
```

调用转换接口：

```bash
curl -X POST "http://localhost:8080/lowflow/model/download" \
  -H "Content-Type: application/json" \
  --data-binary @process.json \
  --output leave-process.bpmn20.xml
```

接口返回 XML 附件，文件名根据流程的 `name` 生成。

## 支持的节点

| JSON `type` | BPMN 元素 |
|---|---|
| `start` | 开始事件 |
| `approval` | 用户任务 |
| `cc` | 使用 `${ccDelegate}` 的服务任务 |
| `exclusive` | 排他网关 |
| `condition` | 条件顺序流 |
| `timer` | 中间定时捕获事件 |
| `notify` | 使用 `${notifyDelegate}` 的异步服务任务 |
| `service` | 可配置的服务任务 |
| `end` | 结束事件 |

注意事项：

- 顺序节点通过 `next` 连接，子节点的 `pid` 必须等于父节点的 `id`。
- `condition` 节点作为 `exclusive` 节点的分支使用。
- 并行节点类尚未实现，也没有注册为可用的 JSON 节点类型。
- 使用抄送或通知节点时，部署生成的 XML 前需要在业务项目中提供 `ccDelegate` 或 `notifyDelegate` Bean。

## 项目结构

```text
src/main/java/com/lowflow/
├── LowflowApplication.java          # Spring Boot 启动类
├── controller/
│   └── ProcessModelController.java  # 转换下载接口
└── pojo/
    ├── ProcessModel.java            # JSON 模型转 BPMN
    ├── condition/                    # 条件表达式模型
    ├── enums/                        # 节点选项枚举
    └── node/                         # BPMN 节点转换器
```

## 演示与相关项目

- [在线设计器](https://tsai996.github.io/lowflow-design/)
- [产品案例](https://demo.lowflow.vip/)

| 托管平台 | 转换器后端 | 设计器前端 |
|---|---|---|
| GitHub | [lowflow-design-converter](https://github.com/tsai996/lowflow-design-converter) | [lowflow-design](https://github.com/tsai996/lowflow-design) |
| 码云 | [lowflow-design-converter](https://gitee.com/cai_xiao_feng/lowflow-design-converter) | [lowflow-design](https://gitee.com/cai_xiao_feng/lowflow-design) |

### 转换前后对比

<p align="center">
  <img alt="低代码流程设计" src="public/lowflow.png" width="48%" />
  <img alt="生成的 BPMN 流程图" src="public/bpmn-img.png" width="48%" />
</p>

## 社区交流

添加微信好友时备注“加群”，或直接加入 QQ 群。

<p>
  <img alt="微信" src="public/wx.jpg" width="240" height="400" />
  <img alt="QQ 群" src="public/qq_qun.jpg" width="240" height="400" />
</p>

**求一份内推岗位**

## 赞助

开源维护不易，如果该项目对您有帮助，您可以请作者喝杯奶茶。

<p>
  <img alt="微信支付" src="public/wxpay.png" width="240" height="240" />
  <img alt="支付宝" src="public/alipay.png" width="240" height="240" />
</p>

## 推荐阅读

贺波老师的[《深入 Flowable 流程引擎：核心原理与高阶实战》](https://item.jd.com/14804836.html)由 Flowable 创始人 Tijs Rademakers 作序，内容覆盖 Flowable 基础原理与高阶实践。

<img alt="《深入 Flowable 流程引擎》图书" src="public/flowable.jpg" width="360" />

## 许可证

本项目采用 [GNU General Public License v3.0](LICENSE) 许可证。
