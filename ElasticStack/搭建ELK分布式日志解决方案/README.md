### 克隆一台虚拟机
- 从之前装好jdk的centos虚拟机快照克隆一台专门搭建elk环境

![](imgs/clone-vm.png)
- 克隆完成以后开机，登录
- 修改主机名
```shell
vim /etc/hostname
zmzhou-132-elk
```
- 修改ip地址
```shell
vim /etc/sysconfig/network-scripts/ifcfg-ens33
TYPE=Ethernet
PROXY_METHOD=none
BROWSER_ONLY=no
BOOTPROTO=static
DEFROUTE=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=yes
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_FAILURE_FATAL=no
IPV6_ADDR_GEN_MODE=stable-privacy
NAME=ens33
#UUID=d77bb448-a7db-4b0f-9812-b306e44c5d3b
DEVICE=ens33
ONBOOT=yes
IPADDR=192.168.163.132
GATEWAY=192.168.163.2
NETMASK=255.255.255.0
DNS1=8.8.8.8
DNS2=114.114.114.114
```
- 重启 `reboot`
- 检查IP，网络和Java环境

![](imgs/ipaddr.png)
如果Java环境没有配好，请参考 [jdk1.8商用免费版下载地址](/程序员之友/提高生产力必须知道的网站/README.md)
下载，并配置环境变量：
```shell
vim /etc/profile
# 在最后添加如下内容：ZZ保存退出以后执行 source /etc/profile
export JAVA_HOME=/usr/java/jdk1.8.0_202
export JRE_HOME=$JAVA_HOME/jre
export PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin
export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export ES_JAVA_HOME=/home/elastic/elasticsearch-7.12.1/jdk
```
### 安装 elasticsearch
- `lscpu` 查看系统架构

![](imgs/lscpu.png)
- 下载相应版本的 elasticsearch [https://www.elastic.co/cn/downloads/elasticsearch](https://www.elastic.co/cn/downloads/elasticsearch)
  
![](imgs/elasticsearch.png)

- 创建用户elastic `useradd elastic` 上传安装包到 `/home/elastic/` 目录下
- 解压，修改配置文件，启动
```shell
tar -zxvf elasticsearch-7.12.1-linux-x86_64.tar.gz 
cd elasticsearch-7.12.1/
vim config/elasticsearch.yml 
# 修改如下内容
cluster.name: zmzhou-132-elk
node.name: es-node-1
path.data: /home/elastic/elasticsearch-7.12.1/data
path.logs: /home/elastic/elasticsearch-7.12.1/logs
bootstrap.memory_lock: false
bootstrap.system_call_filter: false
network.host: 0.0.0.0
http.port: 9200
discovery.seed_hosts: ["127.0.0.1", "zmzhou-132-elk"]
cluster.initial_master_nodes: ["es-node-1"]
```
```shell
# 修改文件夹所属用户权限
chown -R elastic:elastic /home/elastic/
# 切换用户
su - elastic
cd elasticsearch-7.12.1/
# 后台启动
./bin/elasticsearch -d
```
- 报错解决办法
- 报错1 `JAVA_HOME is deprecated, use ES_JAVA_HOME`
  
![](imgs/error1.png)
```shell
vim /etc/profile
export ES_JAVA_HOME=/home/elastic/elasticsearch-7.12.1/jdk
source /etc/profile
```
- 报错2 
```text
[2] bootstrap checks failed. You must address the points described in the following [2] lines before starting Elasticsearch.
bootstrap check failure [1] of [2]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
bootstrap check failure [2] of [2]: the default discovery settings are unsuitable for production use; at least one of [discovery.seed_hosts, discovery.seed_providers, cluster.initial_master_nodes] must be configured
```
```shell
# echo "vm.max_map_count=262144" >> /etc/sysctl.conf
# sysctl -p  #使修改立即生效
```
- 查看日志 `tail -100f /home/elastic/elasticsearch-7.12.1/logs/zmzhou-132-elk.log `, 启动成功如下：
  
![](imgs/es-started.png)
![](imgs/es-success.png)
### 安装 Logstash
- 下载相应版本的 Logstash [https://www.elastic.co/cn/downloads/logstash](https://www.elastic.co/cn/downloads/logstash)

![](imgs/Logstash.png)
- 解压，修改配置文件，启动
```shell
tar -zxvf logstash-7.12.1-linux-x86_64.tar.gz 
cd logstash-7.12.1/
cp config/logstash-sample.conf config/logstash.conf
vim startup.sh
# 编辑如下内容，保存退出
#!/bin/bash
nohup ./bin/logstash -f config/logstash.conf &
chmod +x startup.sh
```

### 安装 Kibana
- 下载相应版本的 Kibana [https://www.elastic.co/cn/downloads/kibana](https://www.elastic.co/cn/downloads/kibana)
  
![](imgs/Kibana.png)
- 解压，修改配置文件，启动
```shell
tar -zxvf kibana-7.12.1-linux-x86_64.tar.gz 
cd kibana-7.12.1-linux-x86_64/
vim config/kibana.yml 
# 修改如下内容：
server.port: 5601
server.host: "0.0.0.0"
server.name: "zmzhou-132-elk"
elasticsearch.hosts: ["http://localhost:9200"]
kibana.index: ".kibana"
# 后台启动
nohup ./bin/kibana &
```
- 启动成功访问：`http://192.168.163.132:5601/`

![](imgs/elk-home.png)

- JVM Support Matrix [https://www.elastic.co/cn/support/matrix#matrix_jvm](https://www.elastic.co/cn/support/matrix#matrix_jvm)

### 防火墙相关命令
```shell
# 启动：
systemctl start firewalld
# 查看状态：
systemctl status firewalld 
firewall-cmd --state
# 停止：
systemctl disable firewalld
#禁用：
systemctl stop firewalld
#查看所有打开的端口
firewall-cmd --zone=public --list-ports
#添加一个端口
firewall-cmd --zone=public --add-port=80/tcp --permanent
firewall-cmd --zone=public --add-port=5601/tcp --permanent
#删除一个端口
firewall-cmd --zone=public --remove-port=80/tcp --permanent
#更新防火墙规则
firewall-cmd --reload
```
