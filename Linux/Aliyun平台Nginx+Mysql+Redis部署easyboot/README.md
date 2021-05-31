### 注册阿里云，免费申领一台云服务器
- 地址 [https://free.aliyun.com/?spm=5176.10695662.7708050970.1.28142c4fKrKBP8](https://free.aliyun.com/?spm=5176.10695662.7708050970.1.28142c4fKrKBP8)
- 新人特惠-购买一台云服务器ECS [https://www.aliyun.com/activity/new?spm=5176.12901015.d71.d71.4ea4525cvsDqbO&scm=20140722.3873.7.3972](https://www.aliyun.com/activity/new?spm=5176.12901015.d71.d71.4ea4525cvsDqbO&scm=20140722.3873.7.3972)

### 安装jdk，配置环境变量
- 下载，上传jdk-8u202-linux-x64.tar.gz
- 解压
```shell
tar -zxvf jdk-8u202-linux-x64.tar.gz -C /usr/java/
```
- 配置环境变量 `vim /etc/profile`
```shell
export JAVA_HOME=/usr/java/jdk1.8.0_202
export JRE_HOME=$JAVA_HOME/jre
export PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin
export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
```
- 刷新环境变量 `source /etc/profile`，检查环境变量配置是否正确 `java -version`

![](imgs/java-version.png)

### [安装Nginx](/Linux/Nginx安装/README.md)
- [设置Nginx开机启动](/Linux/设置Nginx开机启动/README.md)

### [安装Redis](/Redis/Redis安装/README.md)
- [设置Redis开机启动](/Redis/设置Redis开机启动/README.md)

### 安装 MySQL
- 下载 Yum 资源包 [https://dev.mysql.com/downloads/repo/yum/](https://dev.mysql.com/downloads/repo/yum/)
- 使用 yum 命令安装 MySQL
```shell
rpm -ivh mysql80-community-release-el8-1.noarch.rpm
yum update -y
yum install mysql-server -y
```
![](imgs/yum-install-mysql1.png)
![](imgs/yum-install-mysql2.png)

- 权限设置：
```shell
chown -R mysql:mysql /var/lib/mysql
chmod -R 777 /var/lib/mysql
```
- 初始化 MySQL：
```shell
mysqld --initialize
```
- 设置开机启动，并启动 MySQL：
```shell
systemctl enable mysqld
systemctl start mysqld
```
- 查看 MySQL 运行状态：
```shell
systemctl status mysqld
```
- 查看MySQL初始密码：
```shell
cat /var/log/mysql/mysqld.log | grep password
#或者
grep 'temporary password' /var/log/mysql/mysqld.log
```

![](imgs/mysql-passwd.png)
  
- 修改密码，设置允许远程登录
```shell
mysql -u root -p
ALTER USER USER() IDENTIFIED BY 'Zmzhou.V587';
use mysql;
update user set user.Host='%' where user.User='root';
flush privileges;
quit;
```
![](imgs/mysql-change-passwd.png)

### 部署 easyboot 后台管理系统
- 创建数据库和用户（只需创建数据库和用户，建表语句由flyway管理，启动服务会自动执行）

```mysql
use mysql;
-- 创建数据库
CREATE DATABASE easyboot;
-- 创建用户
create user 'easyboot'@'%' identified by 'Zmzhou.V587';
-- 授权 或者赋所有权限 grant all privileges on `easyboot`.* to 'easyboot'@'%';
grant select,insert,update,delete,create,drop on `easyboot`.* to 'easyboot'@'%';
-- 查看一下用户列表
select host,user,authentication_string from mysql.user;
-- 刷新权限，立即启用修改
flush privileges;
```

- git clone https://gitee.com/zmzhou-star/easyboot.git
- 修改redis等配置文件
- 打包
```shell
mvn clean install -X -Dmaven.test.skip=true -P prod
```
- 新建Linux后台运行启动脚本 startup.sh
```shell
#!bin/sh
nohup java -jar -server easyboot-1.0.jar >/dev/null 2>&1 &
```

- 值得注意的是阿里云服务器WAF防火墙不放行**25端口**，所以发邮件需要使用 **465端口** 或者 **587端口**，设置开启**ssl**
```yaml
spring: 
  mail: 
    host: smtp.qq.com #SMTP服务器地址
    # smtp的指定端口 使用465要将protocol改为 smtps 587端口可以使用默认协议smtp
    port: 465
    protocol: smtps
    username: zmzhou818 #登陆账号
    password: 123456 #登陆密码（或授权码）
    properties:
      from: zmzhou818@qq.com #邮件发信人（即真实邮箱）
      # 设置开启ssl
      mail:
        stmp:
          ssl:
            enable: true
```

#### 添加Nginx反向代码配置
```text
location /eboot {
    proxy_pass http://127.0.0.1:8089/eboot;
}
error_page  404              /404.html;
location = /404.html {
    # 放错误页面的目录路径。
    root   html;
}
```

#### 部署vue前端
- 打包
```shell
cd vue-easyboot
npm install --registry=https://registry.npm.taobao.org
#构建生产环境
npm run build:prod
```

- 压缩上传 **dist** 文件夹下的文件到 nginx 的 **html** 文件夹下解压

#### 浏览器地址栏访问：**http://120.78.87.244**
![](imgs/easyboot.png)

记得要在阿里云安全组添加**80端口**等放行策略

![](imgs/aliyun-security.png)