# Spring Cloud Alibaba

官方文档：https://spring-cloud-alibaba-group.github.io/github-pages/2021/en-us/index.html

## 基础

网关：Gateway，动态路由、过滤器、鉴权   
服务发现：Nacos，可切换AP/CP模式，注册中心 + 配置中心  
流量监控、熔断：Sentinel  
分布式消息系统：RocketMq

## Gateway

Netty运行，非阻塞式模型，常用filter-参数校验、路径处理、参数修改、鉴权、修改响应头、统计耗时、限流    
执行流程：  
1.netty接收请求  
2.获取匹配的Route  
3.获取对应的Filter Chain（过滤器执行链）  
4.按照@order顺序执行Pre逻辑  
5.代理转发给真正的微服务
6.处理返回，执行Filter的Post逻辑  
7.返回结果给客户端

## Nacos

服务发现：服务启动时发送rest请求注册，Nacos内部通过map存储映射关系  
心跳检测：客户端定时发送心跳请求，异常时会告警并进行流量控制  
服务拉取：上下游获取可用依赖服务列表信息，UDP机制感知变化（推送+轮训）  
一致性：支持Raft（CP）和Distro（AP）

## Sentinel

责任链模式，核心由多个Slot组成SlotChain  
NodeSelectorSlot：负责收集资源路径，并将这些资源以树状结构存储，用于根据调用链路进行流量控制。  
ClusterBuilderSlot：负责建立集群节点，用于存储资源的运行统计数据（QPS、RT 等）。  
StatisticSlot（核心）：这是 Sentinel 的数据源。它负责实时统计资源的所有指标数据（成功数、异常数、响应时间）。  
FlowSlot：根据预设的限流规则（QPS、线程数）和 StatisticSlot 中的实时数据，决定是否放行。  
DegradeSlot：根据熔断规则（如慢调用比例、异常比例）进行拦截。

## 问答

Nacos集群挂了，微服务还能正常调用吗？可以，客户端有服务列表信息缓存   
分布式事务？  
1.Seata，保存sql执行的undoLog，成功删除记录，失败根据记录进行数据还原  
2.TCC，try-尝试检测业务状态，冻结资源；Confirm-进行业务执行（需要幂等）；Cancel-释放资源  