### 简介
- Pulsar 是一个用于服务器到服务器的消息系统，具有多租户、高性能等优势。 Pulsar 最初由 Yahoo 开发，目前由 [Apache 软件基金会](https://www.apache.org/) 管理。
Pulsar 的关键特性如下：
  - Pulsar 的单个实例原生支持多个集群，可跨机房在集群间无缝地完成消息复制。
  - 极低的发布延迟和端到端延迟。
  - 可无缝扩展到超过一百万个 topic。
  - 简单的客户端 API，支持 Java、Go、Python 和 C++。
  - 支持多种 topic 订阅模式（独占订阅、共享订阅、故障转移订阅）。
  - 通过 Apache BookKeeper 提供的持久化消息存储机制保证消息传递 。
    - 由轻量级的 serverless 计算框架 Pulsar Functions 实现流原生的数据处理。
  - 基于 Pulsar Functions 的 serverless connector 框架 Pulsar IO 使得数据更易移入、移出 Apache Pulsar。
  - 分层式存储可在数据陈旧时，将数据从热存储卸载到冷/长期存储（如S3、GCS）中。
- Apache Pulsar 是 Apache 软件基金会顶级项目，是下一代云原生分布式消息流平台，集消息、存储、轻量化函数式计算为一体，采用计算与存储分离架构设计，支持多租户、持久化存储、多机房跨区域数据复制，具有强一致性、高吞吐、低延时及高可扩展性等流数据存储特性，被看作是云原生时代实时消息流传输、存储和计算最佳解决方案