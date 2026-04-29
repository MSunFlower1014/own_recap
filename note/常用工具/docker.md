# docker

## 基础指令

docker info 查询全局信息

```shell
mayantao@anonymous ~ % docker info
Client:
 Version:    27.4.0
 Context:    desktop-linux
 Debug Mode: false
 Plugins:
  ai: Ask Gordon - Docker Agent (Docker Inc.)
    Version:  v0.5.1
    Path:     /Users/mayantao/.docker/cli-plugins/docker-ai
  buildx: Docker Buildx (Docker Inc.)
    Version:  v0.19.2-desktop.1
    Path:     /Users/mayantao/.docker/cli-plugins/docker-buildx
  compose: Docker Compose (Docker Inc.)
    Version:  v2.31.0-desktop.2
    Path:     /Users/mayantao/.docker/cli-plugins/docker-compose
  debug: Get a shell into any image or container (Docker Inc.)
    Version:  0.0.37
    Path:     /Users/mayantao/.docker/cli-plugins/docker-debug
  desktop: Docker Desktop commands (Beta) (Docker Inc.)
    Version:  v0.1.0
    Path:     /Users/mayantao/.docker/cli-plugins/docker-desktop
  dev: Docker Dev Environments (Docker Inc.)
    Version:  v0.1.2
    Path:     /Users/mayantao/.docker/cli-plugins/docker-dev
  extension: Manages Docker extensions (Docker Inc.)
    Version:  v0.2.27
    Path:     /Users/mayantao/.docker/cli-plugins/docker-extension
  feedback: Provide feedback, right in your terminal! (Docker Inc.)
    Version:  v1.0.5
    Path:     /Users/mayantao/.docker/cli-plugins/docker-feedback
  init: Creates Docker-related starter files for your project (Docker Inc.)
    Version:  v1.4.0
    Path:     /Users/mayantao/.docker/cli-plugins/docker-init
  sbom: View the packaged-based Software Bill Of Materials (SBOM) for an image (Anchore Inc.)
    Version:  0.6.0
    Path:     /Users/mayantao/.docker/cli-plugins/docker-sbom
  scout: Docker Scout (Docker Inc.)
    Version:  v1.15.1
    Path:     /Users/mayantao/.docker/cli-plugins/docker-scout

Server:
ERROR: Cannot connect to the Docker daemon at unix:///Users/mayantao/.docker/run/docker.sock. Is the docker daemon running?
errors pretty printing info
```

docker version 查看版本  
docker image 查看已有镜像  
docker ps 查询运行中容器  
docker pull <iamge> <tag> 从仓库拉取镜像  
docker run -d -p 8080:80 --name my_name <image> 后台运行并映射端口  
docker exec -it <id> /bin/bash 进入容器内部交互  
docker logs -f --tail 100 <id> 查看容器日志  
docker network ls 查看网络列表  
docker volume ls 查看数据卷  
docker stats 查看容器资源消耗

## dockerFile示例

```shell
# --- 第一阶段：构建环境 ---
# 使用轻量级 JDK 21 基础镜像
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# 设置工作目录
WORKDIR /app

# 利用 Docker 层缓存：先只拷贝 pom.xml 下载依赖
COPY pom.xml .
# 如果是国内环境，可以在此处配置 Maven 镜像源
RUN mvn dependency:go-offline

# 拷贝源码并打包
COPY src ./src
RUN mvn clean package -DskipTests

# --- 第二阶段：运行环境 ---
# 生产环境只需 JRE，不需要完整的 JDK
FROM eclipse-temurin:21-jre-alpine

# 设置时区（Java 应用常见问题）
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

WORKDIR /app

# 从构建阶段拷贝编译好的 jar 包
COPY --from=build /app/target/*.jar app.jar

# 创建非 root 用户运行应用，增强安全性
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# 暴露端口
EXPOSE 8080

# 环境变量配置
# 关键：开启虚拟线程支持并优化 JVM
ENV JAVA_OPTS="-XX:+UseZGC -XX:MaxRAMPercentage=75.0 -Dspring.threads.virtual.enabled=true"

# 使用 ENTRYPOINT 以便接收 SIGTERM 信号进行优雅停机
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

构建流程：  
1.构建镜像：docker build -t user-service:v1 .  
2.运行（指定端口、内存占用、cpu信息）：docker run -d \
--name user-service \
-p 8080:8080 \
--memory="1g" --cpus="2" \
user-service:v1

## 实战问题

如何减小镜像体积？  
1.使用精简基础镜像，如alpine或者debian-slim而不是ubuntu  
2.多阶段编译，将源码和构建提前，只处理jar文件  
3.合并run指令，例如：将apt-get update和install写在同一行  
4.排除不必要文件  
容器和虚拟机（VM）有何不同？  
1.VM是硬件级别各级，独立的内核；容器时进程隔离，容器内共享内核  
2.容器轻量启动秒级，VM启动分钟级，占用大量内存  
3.Docker依赖linux内核的NameSpaces（环境隔离）和Cgroups（资源限制）  
持久化？
分为挂载数据卷（Volume）和绑定挂载（Bind Mount）；  
Volume：由 Docker 管理，存储在 /var/lib/docker/volumes/更安全；  
Bind Mount：将主机任意路径挂载到容器。依赖主机目录结构，通常用于开发环境  
