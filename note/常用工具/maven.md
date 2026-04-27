# maven

## 常用命令

mvn clean - 清理项目  
mvn build - 编译项目  
mvn test - 执行测试  
mvn install - 安装依赖  
mvn dependency:tree - 查看依赖树  
参数-DskipTests - 跳过测试

## 代理配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.2.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.2.0 https://maven.apache.org/xsd/settings-1.2.0.xsd">

    <mirrors>
        <mirror>
            <id>aliyunmaven</id>
            <mirrorOf>central</mirrorOf>
            <name>阿里云公共仓库</name>
            <url>https://maven.aliyun.com/repository/public</url>
        </mirror>
    </mirrors>

    <!-- 可选：设置本地仓库位置（默认在 ~/.m2/repository） -->
    <!-- <localRepository>/Users/你的用户名/.m2/repository</localRepository> -->
</settings>
```