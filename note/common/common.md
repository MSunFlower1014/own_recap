# 通用

## Tcp和Udp

Tcp面向连接，Udp面向无连接  
Tcp保证数据传输Udp不保证可靠交付  
Udp没有阻塞控制，网络阻塞不会影响后续数据传输（用于ip电话，视频通信）  
Tcp是点到点，Udp支持1对一，一对多和多对多

## http响应码

101-切换协议，如升级为websocket  
200-成功  
201-创建成功  
304-资源未修改，直接使用缓存即可  
401-请求用户进行登陆认证  
403-服务器拒绝请求，常见于服务器繁忙或被拉黑  
404-not found  
408-服务器等待客户端发送请求超时  
413-请求体过大，常见于文件上传超过限制  
500-服务器内部错误  
502-无效响应  
504-网关超时  
1\**服务器接收请求需要进一步操作  
2\**成功类  
3\**重定向  
4\**客户端错误  
5\**服务器错误

## 请求信息

post和put区别：put需要幂等，多次请求结果不变，post多次请求结果可能不同  
keep-alive作用：保持连接，减少握手挥手消耗，利于数据批量传输  
cookie和session的区别：cookie是浏览器保持会话的一种机制，可以用来实现session。  
session是服务端的会话概念，用来跟踪用户状态，可以用cookie、请求url参数等方式实现。  
分布式session：
1.前端保存并传入id，后端通过redis等数据库保存并读取。  
2.JWT 的全称是 JSON Web Token，通过编码传递session信息，如Authorization：\*******
http长链接如何判断数据接收完毕：1.content-length指定数据长度。2.分块传输时在每块数据前指定长度，且最后以0长度的分块结尾。  
tcp如何保证数据传输准确：校验和、请求序号和ack、超时重传

## TCP三次握手

确认双方网络畅通，确定双方报文序号以保证有序性，且可以过滤无效报文

1. 客户端主动打开，发送连接请求报文段，将SYN标识位置为1，Sequence Number置为x（TCP规定SYN=1时不能携带数据，x为随机产生的一个值），然后进入SYN_SEND状态。
2. 服务器收到SYN报文段进行确认，将SYN标识位置为1，ACK置为1，Sequence Number置为y，Acknowledgment
   Number置为x+1，然后进入SYN_RECV状态，这个状态被称为半连接状态
3. 客户端再进行一次确认，将ACK置为1（此时不用SYN），Sequence Number置为x+1，Acknowledgment
   Number置为y+1发向服务器，最后客户端与服务器都进入ESTABLISHED状态  
   三次握手可以防止已经失效的连接请求报文段突然又传回到服务端而产生错误的场景：所谓"已失效的连接请求报文段"是这样产生的。

## TCP四次挥手

• 客户端发送一个报文给服务端（没有数据），其中FIN设置为1，Sequence Number置为u，客户端进入FIN_WAIT_1状态  
• 服务端收到来自客户端的请求，发送一个ACK给客户端，Acknowledge置为u+1，同时发送Sequence
Number为v，服务端年进入CLOSE_WAIT状态  
•
服务端发送一个FIN给客户端，ACK置为1，Sequence置为w，Acknowledge置为u+1，用来关闭服务端到客户端的数据传送，服务端进入LAST_ACK状态  
• 客户端收到FIN后，进入TIME_WAIT状态，接着发送一个ACK给服务端，Acknowledge置为w+1，Sequence
Number置为u+1，最后客户端和服务端都进入CLOSED状态  
前两次确认客户端所有数据发送完毕，后两次确定服务器所有数据发送完毕，实现双向通信关闭

## https加密

获取证书-> 检验证书域名等信息->通过交互中的三个随机数生成会话密钥此密钥通过非对称证书加密->之后的链接通过会话密钥对称加密


