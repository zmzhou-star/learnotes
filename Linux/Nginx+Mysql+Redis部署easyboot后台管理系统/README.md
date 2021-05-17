### [安装Nginx](/Linux/Nginx安装/README.md)
- [设置Nginx开机启动](/Linux/设置Nginx开机启动/README.md)

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

### [安装Redis](/Redis/Redis安装/README.md)
- [设置Redis开机启动](/Redis/设置Redis开机启动/README.md)
