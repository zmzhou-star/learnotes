### Tomcat 安装
- 官网下载地址： [https://tomcat.apache.org/download-90.cgi](https://tomcat.apache.org/download-90.cgi)
- 解压 `tar -zxvf apache-tomcat-9.0.46.tar.gz`
- 删除 **webapps** 文件夹下内容

### 禁用AJP连接

- Tomcat 的 **conf/server.xml** 中配置了两种连接器。
  - HTTP Connector 拥有这个连接器，Tomcat才能成为一个web服务器，但还额外可处理Servlet和jsp。
    - 是其接收HTTP请求的关键模块, 可以通过它来指定IO处理模式, 指定处理该Connector接收到的请求的线程数, 以及其他常用的HTTP策略.
  - AJP Connector AJP连接器可以通过AJP协议和另一个web容器进行交互。
    - AJP(Apache JServer Protocol)是为 Tomcat 与 HTTP 服务器之间通信而定制的协议, 能提供较高的通信速度和效率.
    - 案例说明: AJP v13 协议是面向包的, Web服务器和Servlet容器通过TCP连接来交互, 为了节省 创建Socket的昂贵代价, Web服务器会尝试维护一个永久的TCP连接到Servlet容器, 并在多个请求与响应周期过程内重用该TCP连接.
    - 一般情况下用Tomcat + Nginx，就需要注销掉该连接器，对我们用处不大，而且还有漏洞，发现新版tomcat8以后都默认注释了AJP配置。

```xml
<!-- Define an AJP 1.3 Connector on port 8009 -->
<!--
<Connector protocol="AJP/1.3"
           address="::1"
           port="8009"
           redirectPort="8443" />
-->
```

### Tomcat的3种运行模式
#### BIO - 同步阻塞IO模式
- BIO, 同步阻塞IO, 性能低, 没有经过任何优化处理和支持.
- 服务器实现模式为一个连接一个线程, 即客户端有连接请求时服务器端就需要启动一个线程进行处理, 如果这个连接不做任何事情会造成不必要的线程开销, 当然可以通过 线程池 机制改善.
- 适用场景: BIO方式适用于连接数比较小且固定的架构, 这种方式对服务器资源要求比较高, 有并发局限, JDK1.4之前的唯一选择.

#### NIO - 同步非阻塞IO模式
- 是Java SE 1.4及后续版本提供的一种新的IO操作方式(即java.nio包及其子包). Java NIO是一个基于缓冲区、并能提供非阻塞IO操作的Java API, 因此NIO也被看成是non-blocking IO(非阻塞式IO)的缩写, 它拥有比传统BIO操作更好的并发性能.
- 服务器实现模式为一个请求一个线程, 即客户端发送的连接请求都会注册到多路复用器上, 多路复用器轮询到连接有IO请求时才启动一个线程进行处理.
- 适用场景: 适用于连接数较多且连接比较时间短(轻操作)的架构, 比如聊天服务器. 这种方式的并发性能局限于应用中, 编程比较复杂.
- 目前Tomcat 8.x默认运行在NIO模式下.

#### APR - 可移植运行时模式
- APR(Apache Portable Runtime, Apache可移植运行时), 是Apache HTTP服务器的一个支持库, 它提供了一组映射到底层操作系统的API, 如果操作系统不支持特定功能, APR库将提供仿真. 因此开发人员可以使用APR使程序真正跨平台移植.
- 此模式的安装步骤比较繁琐, 但却从操作系统层面解决了异步IO的问题, 能大幅度提高应用性能.
- APR的本质是使用 JNI 技术调用操作系统底层的IO接口, 所以需要提前安装必要的依赖, 具体方式后续给出.

### Tomcat的并发配置(配置Connector)
#### 使用线程池处理请求
- 自定义线程池配置

```xml
<Executor name="tomcatThreadPool" namePrefix="catalina-exec-" maxIdleTime="60000"
        maxThreads="800" minSpareThreads="10" maxQueueSize="10000" prestartminSpareThreads="true"/>
```

- 线程池参数说明

```text
name: 线程池名称.
namePrefix: 创建的每个线程的名称前缀, 单独的线程名称为 namePrefix + threadNumber.
maxThreads: 线程池中最大并发线程数, 默认值为200, 一般建议设置400~ 800 , 要根据服务器配置和业务需求而定.
minSpareThreads: 最小活跃线程数, 也就是核心线程数, 不会被销毁, 会一直存在.
prestartminSpareThreads: 是否在启动程序时就生成minSpareThreads个线程, 默认为false, 即不启动. 若不设置为true, 则minSpareThreads的设置就不起作用了.
maxIdleTime: 线程最大空闲时间, 超过该时间后, 空闲线程会被销毁, 默认值为6000, 单位为毫秒.
maxQueueSize: 最大的等待队列数, 超过则拒绝请求. 默认值为int类型的最大值(Integer.MAX_VALUE), 等同于无限大. 一般不作修改, 避免发生部分请求未能被处理的情况.
threadPriority: 线程池中线程的优先级, 默认值为5, 取值范围: 1 ~ 10.
className：线程池的实现类, 未指定情况下, 默认实现类为 org.apache.catalina.core.StandardThreadExecutor. 要自定义线程池就需要实现 org.apache.catalina.Executor 接口.
```

### 在Connector中使用线程池
- **Connector** 是Tomcat接收请求的入口, 每个Connector都有自己专属的监听端口.
- 自定义Connector配置

```xml
<Connector executor="tomcatThreadPool" port="8080" protocol="org.apache.coyote.http11.Http11Nio2Protocol" 
           connectionTimeout="60000" redirectPort="8443" enableLookups="false" maxPostSize="-1" URIEncoding="UTF-8" 
		   acceptCount="1000" acceptorThreadCount="2" disableUploadTimeout="true" maxConnections="10000" SSLEnabled="false"/>
```

- Connector的参数说明

```text
redirectPort="8443" # 基于SSL的端口, 在需要基于安全通道的场合, 比如当客户端的请求协议是HTTPS时, 将该请求转发到此端口.
minSpareThreads="25" # Tomcat连接器的最小空闲Socket线程数, 默认值为25. 如果当前没有空闲线程, 且没有超过maxThreads, 将一次性创建的空闲线程数量. Tomcat初始化时创建的线程数量也是此值.
maxSpareThreads="75" # 最大空闲线程数, 一旦创建的线程超过此值, Tomcat就会关闭不再需要的Socket线程, 默认值为50. 线程数可以大致用 "同时在线用户数、用户每秒操作次数、系统平均操作时间" 来计算.
keepAliveTimeout="6000" # 下次请求到来之前, Tomcat保持该连接6000ms.
maxKeepAliveRequests="10" # 该连接最大支持的请求数, 超过该请求数的连接也将被关闭(此时就会返回一个Connection: close头给客户端). 1表示禁用长连接, -1表示不限制连接个数, 默认为100, 一般设置在100~200之间.
acceptorThreadCount="1" # 用于接收连接的线程的数量, 默认值是1. 一般如果服务器是多核CPU时, 需要改配置为 2.
enableLookups="false" # 是否支持反查域名(即DNS解析), 默认为true. 为提高处理能力, 应设置为false.
disableUploadTimeout="true" # 上传时是否启用超时机制, 若为true, 则禁用上传超时.
connectionTimeout="20000" # 网络连接超时时间, 默认值为20000ms, 设置为0表示永不超时 —— 存在隐患. 通常可设置为30000ms.
URIEncoding="UTF-8" # 指定Tomcat容器的URL编码格式.
maxHttpHeaderSize="8192" # HTTP请求头信息的最大程度, 超过此长度的部分不予处理. 一般设置为8K即可.
maxPostSize="10485760" # 指定POST请求的内容大小, 单位为Byte, 默认大小为2097152(2MB), 10485760为10M. 如果要禁用限制, 可设置为-1.
compression="on" # 打开传输时压缩功能.
compressionMinSize="10240" # 启用压缩的输出内容大小, 默认为2048, 即2KB.
noCompressionUserAgents="gozilla, traviata" # 设置不启用压缩的浏览器
compressableMimeType="text/html,text/xml,text/javascript,text/css,text/plain" # 压缩的资源类型
```
