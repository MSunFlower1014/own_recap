# shell

## 常用命令

top 查看系统负载  
du -sh * 磁盘空间排查，du -sh * | sort -hr找出最占空间文件夹  
netstat -tlnp 查询各端口占用  
curl http接口调试  
telnet 检查端口网络  
tail -f -n 200 查看最新的两百行日志  
awk '{print $9, $7}' access.log | sort | uniq -c | sort -rn | head -n 10 快速统计 Nginx 日志中访问量最高的前 10个 URL  
ls -lrt 查看当前目录文件  
rm -rf * 删除文件  
mv * ./ 移动文件

## 服务启动脚本示例

优势：jvm使用ZGC  
pgrep -f 代替 ps -ef ｜ grep java

```shell
#!/bin/bash

# =================================================================
# 描述: Spring Boot 应用程序管理脚本
# 作者: Gemini
# 日期: 2026-04-28
# =================================================================

# --- 配置信息 ---
APP_NAME="your-service-name.jar"
APP_PATH="/home/admin/app"
LOG_PATH="/home/admin/logs/console.log"
# 推荐使用 JDK 21 的虚拟线程，增加 JVM 启动参数
JVM_OPTS="-Xms512m -Xmx1024m -XX:+UseZGC -Dspring.threads.virtual.enabled=true"

# --- 检查应用是否在运行 ---
is_exist() {
    # 使用 pgrep 查找进程 ID
    pid=$(pgrep -f $APP_NAME)
    if [ -z "${pid}" ]; then
        return 1
    else
        return 0
    fi
}

# --- 启动服务 ---
start() {
    is_exist
    if [ $? -eq 0 ]; then
        echo ">>> ${APP_NAME} 已经在运行中 (PID: ${pid}) <<<"
    else
        echo ">>> 正在启动 ${APP_NAME} ... <<<"
        # 后台运行并将输出重定向到日志文件
        nohup java $JVM_OPTS -jar $APP_PATH/$APP_NAME > $LOG_PATH 2>&1 &
        echo ">>> 启动成功，请查看日志: tail -f $LOG_PATH <<<"
    fi
}

# --- 停止服务 ---
stop() {
    is_exist
    if [ $? -eq 0 ]; then
        echo ">>> 正在停止 ${APP_NAME} (PID: ${pid}) ... <<<"
        kill -15 $pid
        sleep 5
        # 再次检查，如果还没停止则强制杀死
        is_exist
        if [ $? -eq 0 ]; then
            echo ">>> 等待超时，强制杀死进程 (PID: ${pid}) <<<"
            kill -9 $pid
        fi
        echo ">>> 服务已成功停止 <<<"
    else
        echo ">>> ${APP_NAME} 未运行，无需停止 <<<"
    fi
}

# --- 查看状态 ---
status() {
    is_exist
    if [ $? -eq 0 ]; then
        echo ">>> ${APP_NAME} 正在运行 (PID: ${pid}) <<<"
    else
        echo ">>> ${APP_NAME} 未运行 <<<"
    fi
}

# --- 帮助说明 ---
usage() {
    echo "使用方法: sh manage.sh [start|stop|restart|status]"
    exit 1
}

# --- 主逻辑 ---
case "$1" in
    "start")
        start
        ;;
    "stop")
        stop
        ;;
    "status")
        status
        ;;
    "restart")
        stop
        start
        ;;
    *)
        usage
        ;;
esac
```