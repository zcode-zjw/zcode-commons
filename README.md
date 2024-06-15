<h1 align="center">zcode-commons后端服务组件库</h1>

<div align="center">

</div>

<hr />

<h3 align="center">

💡 **组件说明** 💡

</h3>

| 组件                | 描述        |
|-------------------|-----------|
| common-auth       | 通用认证限权组件  |
| common-framework  | 通用框架组件    |
| common-config     | 通用配置组件    |
| common-datasource | 通用数据源组件   |
| common-cache      | 通用缓存组件    |
| common-log        | 通用日志组件    |
| common-web        | 通用Web组件   |
| common-utils      | 通用工具类组件   |
| common-correspond | 通用通讯组件    |
| common-middleware | 通用中间件组件   |
| common-test       | 通用测试组件    |
| common-ssh        | 通用ssh组件   |
| common-cloud      | 通用微服务组件   |
| common-file       | 通用文件组件    |
| common-apiword    | 通用接口文档组件  |
| common-minio      | 通用分布式文件组件 |
| common-schedule   | 通用调度组件    |
| common-generator  | 通用生成器组件   |
| common-security   | 通用安全组件    |

#### 配置文件（按需配置） 文件如下：

```
组件主配置文件: zcode.properties 
运维主配置文件: oamp.properties
监控配置文件: omap-monitor.properties
SSH配置文件: omap-ssh.properties
定时任务配置文件: omap-timetask.properties
其他配置文件: omap-pool.properties
```

<hr/>

<h3 align="center">

💡 **目前的领域** 💡

</h3>

| 范围   | 描述                                    | API | 数据来源 | 说明  |
|------|---------------------------------------|-----|------|-----|
| 认证   | 用户认证，可支持token和session                 | -   | -    | -   |
| 限权   | 用户限权                                  | -   | -    | -   |
| 数据源  | 支持Mysql、Oracle、PostgreSql数据库，支持多数据源切换 | -   | -    | -   |
| 数据安全 | 接口传输数据的加解密，支持RSA、AES、DES等多种加密算法       | -   | -    | -   |
| 调度   | 支持原生定时任务、QZ                           |     | -    | -   |
| 配置管理 | 组件和业务配置统一管理                           | -   | -    | -   |
| Web  | 支持数据脱敏、接口防重、全局异常处理、反爬、限流、防XSS、防CSRF等  | -   | -    | -   |
| 文件   | 支持上传、存储、下载                            | -   | -    | -   |
| 通讯   | 支持WebSocket、Netty                     | -   | -    | -   |
| 缓存   | 支持本地缓存与Redis缓存                        |     | -    | -   |
| 中间件  | 支持MQ、canal、email、ES等                  | -   | -    | -   |
| 日志   | 支持操作日志、审计日志、日志追踪等                     | -   | -    | -   |
| 微服务  | 支持nacos、sentinel、seata、gateway等       | -   | -    | -   |
| 低代码  | 支持本地客户端以及Web应用                        | -   | -    | -   |
| 工具   | 涵盖日常开发所用工具类                           | -   | -    | -   |
| 运维   | 支持服务管理、文件监控、端口监控、主机节点等                | -   | -    | -   |
| 部署   | 支持Docker打包                            | -   | -    | -   |
<hr/>

<h3 align="center">

💡 **快速开始** 💡

</h3>

## 环境准备

jdk1.8、Mysql5.7以上

## 依赖安装

安装本地Maven，依赖下载推荐使用华为云镜像仓库，依赖详见主Pom.xml文件

## 脚本初始化

运行doc下的SQL文件

## 配置文件

将config下的文件拷贝至自己的项目resources目录下

## 组件引入

### 引入父依赖

```xml
<parent>
    <groupId>com.zcode.zjw</groupId>
    <artifactId>zcode-commons</artifactId>
    <version>1.0.0</version>
</parent>
```

### 引入组件

```xml
<parent>
    <groupId>com.zcode.zjw</groupId>
    <artifactId>zcode-utls</artifactId>
</parent>
```

### 使用

<hr />

<h3 align="center">
📚 问题 📚
<br />
<br />
如遇到任何问题，欢迎联系我，你的宝贵意见将成为我前进的动力


<h3 align="center">

💡 支持 💡
<br />
<br />
如需关注项目最新动态，请Watch、Star项目，同时也是对项目最好的支持