# Spring AI

官方文档：https://docs.spring.io/spring-ai/reference/

## 基础

模型分类：large language model（大语言模型）、image Generation Model（图像生成）、  
text-to-Voice model/voice-to-text model（文本转语音/语音转文本）、
Embedding model（嵌入模型，嵌入的工作原理是将文本、图像和视频转换为浮点数数组，称为向量）。

主要功能：
1.建立标准，提供多类模型的接入框架，通过配置就可以完成接入并切换简单  
2.advisor 自定义上下文管理。  
3.rag 构建私有知识库。

## Advisor机制

动态修改用户的请求和结果响应，常见场景：
1.Chat Memory，记录历史聊天记录   
2.Logging/Audit，切面，记录日志、统计token。  
3.Safe Guard (安全护栏)，敏感词和合规检测

```java
//聊天记忆内置实现类
var chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory), // 自动处理记忆
                        new QuestionAnswerAdvisor(vectorStore)    // 自动处理 RAG（见下文）
                )
                .build();

```

## Rag

解决模型知识时效性、私有化数据安全、以及降低生成幻觉   
流程：
1.离线阶段：读取文档（内部信息）- 文档拆分 - 转为数学向量 - 保存响亮。  
2.在线阶段：用户提问 - 问题向量化 - 匹配最佳的文档分块 - 将问题与文档发给大模型 - 得出结果。  
