# redis

## 基本数据类型

String\List\Set\SortSet\Hash  
SortSet底层由跳表实现(数量小于512且数据为数字时为有序数组)

## redis为什么快

1.内存操作  
2.单线程，无需切换上下文  
3.非阻塞I/O多路复用