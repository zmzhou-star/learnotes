- 管理员身份运行cmd直接启用已知服务名的服务命令如下：
  - net start [服务名] 启动一个服务 例如：net start MySQL
  - net stop [服务名] 停用一个服务
  
|Win+r或者搜索|说明|
| :---: | :---: |
| services.msc |打开本地服务管理器|
| calc |启动计算器|
| diskmgmt.msc|磁盘管理实用程序|
| chkdsk.exe |Chkdsk磁盘检查|
| devmgmt.msc | 设备管理器|
| notepad | 打开记事本|
| charmap | 启动字符映射表|
| regedt32/regedit |注册表编辑器|
| Msconfig.exe|系统配置实用程序|
| mspaint|画图板|
| mstsc|远程桌面连接|
| cleanmgr  |垃圾整理 |
| control   | 启动控制面板|
| osk|打开屏幕键盘|
| odbcad32|ODBC数据源管理器|
| dfrg.msc| 磁盘碎片整理程序|
| charmap | 启动字符映射表|

- 关机命令 shutdown 的使用技巧：
  - 用法: shutdown [/i | /l | /s | /sg | /r | /g | /a | /p | /h | /e | /o] [/hybrid] [/soft] [/fw] [/f]
  [/m \\computer][/t xxx][/d [p|u:]xx:yy [/c "comment"]]
    
  - 立即关机：`shutdown -s -t 0`
  - 10秒后关机并提示：`shutdown -s -t 10 -c "The computer will shut down in 10 seconds"`
  - 定时关机，比如12点关机：`at 12:00 shutdown -s`
  - 取消自动关机,在运行中输入：`shutdown -a`
  - 关机重启：`shutdown -r`
  - at命令使用条件：必须开启Task scheduler服务（开启的方法在命令界面输入 `net start schedule` ，关闭输入`net stop schedule`）
  
- 关闭445,135,137,138,139,3389端口脚本
```bat
set PORT=445,135,137,138,139,3389
set RULE_NAME="1远程连接端口：%PORT% 入栈规则"
netsh advfirewall firewall show rule name=%RULE_NAME% >nul
if not ERRORLEVEL 1 (
rem 对不起，规则 %RULENAME% 已经存在
) else (
echo 规则 %RULENAME% 创建中...
netsh advfirewall firewall add rule name=%RULE_NAME% dir=in action=block protocol=TCP localport=%PORT%
)
@pause
```
用法：新建bat文件，以管理员身份运行