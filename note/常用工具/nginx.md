# nginx

## 基础信息

异步，非阻塞，多路复用，nio，减少上下文切换  
一个master进程和多个worker进程，worker进程通过epoll多路复用，一个进程可以处理上万个连接

## 常用命令

nginx -s quit 优雅停止，等待连接请求结束  
nginx -s reload 热加载  
nginx -c /path/nginx.conf 启动并指定配置文件  
nginx -t 检查配置文件语法是否有误  
自动化脚本推荐：nginx -t && nginx -s reload 避免配置文件错误导致网管宕机

## 配置

正向代理和反向代理？  
正向代理：隐藏 客户端。客户端通过代理服务器去访问外部互联网（如 VPN）。Nginx 作为客户端的代表。  
反向代理：隐藏 服务端。客户端直接请求 Nginx，Nginx 根据配置分发到内部微服务。Nginx 作为服务端的门户。

负债均衡？  
1.轮询（round-robin，默认）：按时间顺序逐一分配。  
2.权重（weight）：根据服务器性能配置比例。  
3.IP Hash：根据客户端 IP 分配，解决 Session 保持问题。  
4.Least Conn（最小连接数）：优先分发给当前压力最小的服务器。  
5.Fair（响应时间）：按后端服务器响应时间分配（需插件）

如何优化nginx以支持高并发？  
1.调整Worker数：通常设为CPU核心数   
2.最大连接数改为65535  
3.开启高效传输，sendfile on （零拷贝）和 tcp_nopush on  
4.Keepalive，配置长连接超时，减少三次握手次数  
5.Gzip压缩，节省静态资源和带宽

安全配置？  
1.隐藏版本号，server_tokens off   
2.限制请求频率：使用 limit_req 模块防御暴力攻击

## 配置文件示例

```shell
user  nginx;
worker_processes  auto; # 自动根据 CPU 核心数启动 Worker
worker_cpu_affinity auto; # 自动绑定 CPU 亲和性

error_log  /var/log/nginx/error.log warn;
pid        /var/run/nginx.pid;

# 每个进程能打开的最大文件描述符
worker_rlimit_nofile 65535;

events {
    use epoll; # 使用高性能事件模型
    worker_connections  10240; # 单个进程最大连接数
    multi_accept on; # 允许同时接收多个新连接
}

http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;

    # 隐藏版本号，安全性优化
    server_tokens off;

    # 性能优化参数
    sendfile        on; # 零拷贝技术
    tcp_nopush      on; # 数据包累积到一定大小再发送，提高效率
    tcp_nodelay     on; # 禁用 Nagle 算法，减少延迟
    
    keepalive_timeout  65;
    types_hash_max_size 2048;

    # 开启 Gzip 压缩，节省带宽
    gzip on;
    gzip_disable "msie6";
    gzip_vary on;
    gzip_proxied any;
    gzip_comp_level 6;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml application/xml+rss text/javascript;

    # 包含具体的站点配置
    include /etc/nginx/conf.d/*.conf;
    
    # 1. 定义后端负载均衡池
    upstream backend_servers {
        server 192.168.1.10:8080 weight=5; # 业务服务器 A
        server 192.168.1.11:8080 weight=5; # 业务服务器 B
        keepalive 32; # 保持与后端的长连接
    }
    
    # 静态资源路径（Vue/React 前端项目）
    location / {
        root   /usr/share/nginx/html;
        index  index.html index.htm;
        try_files $uri $uri/ /index.html; # 解决 Vue Router 在 history 模式下刷新 404 问题
    }
    
    # 动态请求路由（反向代理到后端）
    location /api/ {
        proxy_pass http_backend_servers; # 指向上面的 upstream
        
        # 透传真实 IP 和 Host 信息
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # 后端超时时间设置
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;

        # 解决跨域（如果后端没处理，可以在这里加）
        # add_header 'Access-Control-Allow-Origin' '*';
    }
    
    # 静态资源缓存优化
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        expires 30d;
        add_header Cache-Control "public, no-transform";
        access_log off;
    }
}
```