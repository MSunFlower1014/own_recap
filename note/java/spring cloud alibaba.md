# Spring Cloud Alibaba

官方文档：https://spring-cloud-alibaba-group.github.io/github-pages/2021/en-us/index.html

## 基础

网关：Gateway  
服务发现：Nacos，可切换AP/CP模式  
流量监控、熔断：Sentinel  
分布式消息系统：RocketMq

## Nacos

服务发现：服务启动时发送rest请求注册，Nacos内部通过map存储映射关系  
心跳检测：客户端定时发送心跳请求，异常时会告警并进行流量控制  
服务拉取：上下游获取可用依赖服务列表信息，UDP机制感知变化（推送+轮训）  
一致性：支持Raft（CP）和Distro（AP）

## 问答

Nacos集群挂了，微服务还能正常调用吗？可以，客户端有服务列表信息缓存   
分布式事务？
1.Seata，保存sql执行的undoLog，成功删除记录，失败根据记录进行数据还原  
2.TCC，try-尝试检测业务状态，冻结资源；Confirm-进行业务执行（需要幂等）；Cancel-释放资源  