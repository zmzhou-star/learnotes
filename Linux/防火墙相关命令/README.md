### centos6.x及以前版本

1. 查看防火墙状态，哪些端口开放
```shell
service iptables status
#或者
/etc/init.d/iptables status
```
2. 配置防火墙 `vim /etc/sysconfig/iptables`

```text
#Firewall configuration written by system-config-firewall 
#Manual customization of this file is not recommended. 
*filter 
:INPUT ACCEPT [0:0] 
:FORWARD ACCEPT [0:0] 
:OUTPUT ACCEPT [0:0] 
-A INPUT -m state –state ESTABLISHED,RELATED -j ACCEPT
-A INPUT -p icmp -j ACCEPT
-A INPUT -i lo -j ACCEPT
-A INPUT -m state –state NEW -m tcp -p tcp –dport 22 -j ACCEPT
#主要在此处添加开放端口配置
-A INPUT -m state –state NEW -m tcp -p tcp –dport 80 -j ACCEPT
-A INPUT -m state –state NEW -m tcp -p tcp –dport 8080 -j ACCEPT
-A INPUT -j REJECT –reject-with icmp-host-prohibited
-A FORWARD -j REJECT –reject-with icmp-host-prohibited
COMMIT
```

3. 最后重启防火墙使配置生效
```shell
service iptables restart
#或者
/etc/init.d/iptables restart
```

### centos7.x及以后版本

- centos7版本对防火墙进行加强,不再使用原来的iptables,启用firewalld

1. **常用命令**
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
#添加一段端口
firewall-cmd --zone=public --add-port=20-21/tcp --permanent
#删除一个端口
firewall-cmd --zone=public --remove-port=80/tcp --permanent
#更新防火墙规则
firewall-cmd --reload
```
2. firewalld的基本使用
```text
启动： systemctl start firewalld
查状态：systemctl status firewalld
停止： systemctl disable firewalld
禁用： systemctl stop firewalld
在开机时启用一个服务：systemctl enable firewalld.service
在开机时禁用一个服务：systemctl disable firewalld.service
查看服务是否开机启动：systemctl is-enabled firewalld.service
查看已启动的服务列表：systemctl list-unit-files|grep enabled
查看启动失败的服务列表：systemctl --failed
```

3. 配置firewalld-cmd
```text
查看版本： firewall-cmd --version
查看帮助： firewall-cmd --help
显示状态： firewall-cmd --state
查看所有打开的端口： firewall-cmd --zone=public --list-ports
更新防火墙规则： firewall-cmd --reload
查看区域信息: firewall-cmd --get-active-zones
查看指定接口所属区域： firewall-cmd --get-zone-of-interface=eth0
拒绝所有包：firewall-cmd --panic-on
取消拒绝状态： firewall-cmd --panic-off
查看是否拒绝： firewall-cmd --query-panic
```

4. 那怎么开启一个端口呢
```text
firewall-cmd --zone=public(作用域) --add-port=80/tcp(端口和访问类型) --permanent(永久生效)
firewall-cmd --zone=public --add-service=http --permanent
firewall-cmd --reload # 重新载入，更新防火墙规则
firewall-cmd --zone= public --query-port=80/tcp #查看
firewall-cmd --zone= public --remove-port=80/tcp --permanent # 删除
firewall-cmd --list-services
firewall-cmd --get-services
firewall-cmd --add-service=<service>
firewall-cmd --delete-service=<service>
#在每次修改端口和服务后/etc/firewalld/zones/public.xml文件就会被修改,所以也可以在文件中之间修改,然后重新加载
#使用命令实际也是在修改文件，需要重新加载才能生效。
firewall-cmd --zone=public --query-port=80/tcp
firewall-cmd --zone=public --query-port=8080/tcp
firewall-cmd --zone=public --query-port=3306/tcp
firewall-cmd --zone=public --add-port=8080/tcp --permanent
firewall-cmd --zone=public --add-port=3306/tcp --permanent
firewall-cmd --zone=public --query-port=3306/tcp
firewall-cmd --zone=public --query-port=8080/tcp
firewall-cmd --reload  # 重新加载后才能生效
firewall-cmd --zone=public --query-port=3306/tcp
firewall-cmd --zone=public --query-port=8080/tcp
```

5. 参数解释
```text
–add-service #添加的服务
–zone #作用域
–add-port=80/tcp #添加端口，格式为：端口/通讯协议
–permanent #永久生效，没有此参数重启后失效
```

6. 详细用法
```text
firewall-cmd --permanent --zone=public --add-rich-rule='rule family="ipv4" source address="192.168.0.4/24" service name="http" accept' #设置某个ip访问某个服务
firewall-cmd --permanent --zone=public --remove-rich-rule='rule family="ipv4" source address="192.168.0.4/24" service name="http" accept' #删除配置
firewall-cmd --permanent --add-rich-rule 'rule family=ipv4 source address=192.168.0.1/2 port port=80 protocol=tcp accept' #设置某个ip访问某个端口
firewall-cmd --permanent --remove-rich-rule 'rule family=ipv4 source address=192.168.0.1/2 port port=80 protocol=tcp accept' #删除配置
firewall-cmd --query-masquerade # 检查是否允许伪装IP
firewall-cmd --add-masquerade # 允许防火墙伪装IP
firewall-cmd --remove-masquerade # 禁止防火墙伪装IP
firewall-cmd --add-forward-port=port=80:proto=tcp:toport=8080 # 将80端口的流量转发至8080
firewall-cmd --add-forward-port=proto=80:proto=tcp:toaddr=192.168.1.0.1 # 将80端口的流量转发至192.168.0.1
firewall-cmd --add-forward-port=proto=80:proto=tcp:toaddr=192.168.0.1:toport=8080 # 将80端口
```