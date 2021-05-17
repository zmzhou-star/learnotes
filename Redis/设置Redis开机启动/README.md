### 修改redis.conf配置文件中的两项配置
- daemonize yes # 以守护进程方式启动
- supervised systemd # 可以跟systemd进程进行交互
```sh
#其他配置参考
daemonize yes # 以守护进程方式启动
supervised systemd # 可以跟systemd进程进行交互
bind 0.0.0.0 #任意ip都可以连接
protected-mode no #关闭保护，允许非本地连接
port 6379 #端口号
pidfile /var/run/redis_6379.pid #进程守护文件，就是存放该进程号相关信息的地方
dir /usr/local/redis-6.2.2/data/ #db等相关目录位置
logfile "/usr/local/redis-6.2.2/logs/redis_6379.log"
appendonly yes #开启日志形式
requirepass 123456 #密码
```

### CentOS 7.x以后 设置Redis开机启动
1. 新建redis.service
`vim /lib/systemd/system/redis.service`
```shell
[Unit]
Description=Redis In-Memory Data Store
Documentation=https://redis.io/
After=network.target
#
[Service]
Type=forking
ExecStart=/usr/local/redis-6.2.2/src/redis-server /usr/local/redis-6.2.2/redis.conf
ExecStop=/usr/local/redis-6.2.2/src/redis-cli shutdown
ExecReload=/bin/kill -s HUP $MAINPID
Restart=always
#
[Install]
WantedBy=multi-user.target
```

#### 参数说明

|[Unit]|服务的说明|
| :---: | :---: |
|Description|描述服务|
|After|在network.target启动后才启动|
|Documentation|官网(可选)|

|[Service]|服务运行参数的设置|
| :---: | :---: |
|Type=forking|后台运行|
|ExecStart|服务的具体运行命令|
|ExecReload|服务的重启命令|
|ExecStop|服务的停止命令|
|Restart|fail时重启|

|[Install]|运行级别的设置|
| :---: | :---: |
|WantedBy|多用户模式|
|Alias|服务别名(可选)|

2. 设置开机启动，启动redis
- 设置开机自启动：systemctl enable redis
- 关闭开机自动启动：systemctl disable redis
- 启动redis服务：systemctl start redis
- 停止服务：systemctl stop redis
- 重新加载redis配置文件：systemctl reload redis
- 查看所有已启动的服务：systemctl list-units --type=service
- 查看服务当前状态：systemctl status redis

### CentOS 7.x以前 设置Redis开机启动

1. 复制redis启动脚本(redis安装包中包含自启动脚本)
```sh
# cp /usr/local/redis-6.2.2/utils/redis_init_script /etc/init.d/redis
# vim /etc/init.d/redis
# 在文件中#!/bin/sh的下方添加两行注释代码
# chkconfig: 2345 10 90  # 注册开机启动的运行级别 2345是默认启动级别 10代表Start的顺序，90代表Kill（Stop）的顺序
# description: Start and Stop redis 
```

> Linux一般会有7个运行级别：

- 0 - 停机
- 1 - 单用户模式
- 2 - 多用户，但是没有NFS ，不能使用网络
- 3 - 完全多用户模式，我最喜欢的模式
- 4 - 不可用
- 5 - X11 带图形界面的多用户模式
- 6 - 重新启动 （如果将默认启动模式设置为6，Linux将会不断重启）

2. 修改redis安装路径
```sh
# vim /etc/init.d/redis
REDISPORT=6379
EXEC=/usr/local/redis-6.2.2/src/redis-server
CLIEXEC=/usr/local/redis-6.2.2/src/redis-cli
PIDFILE=/var/run/redis_${REDISPORT}.pid
CONF="/usr/local/redis-6.2.2/redis.conf"
```

3. 启动redis
- 启动redis命令:service redis start
- 关闭redis命令:service redis stop
- 设为开机启动:chkconfig redis on
- 关闭开机启动:chkconfig redis off
