## Linux软件安装

### 1. 介绍

Redhat提供了rpm管理体系    已经编译的软件包：针对不同的平台系统编译目标软件包    软件包包含依赖检查，但，还需人为解决

### 2. 命令

(1)    rpm安装： 

①    -ivh filename

②  --prefix 

(2) rpm卸载： 

①    -e PACKAGE_NAME

(3)    其他选项：

①   rpm -qa : 查询已经安装的所有包

②   rpm -q PACKAGE_NAME: 查询指定的包是否已经安装

③   rpm -qi PACKAGE_NAME: 查询指定包的说明信息

④   rpm -ql PACKAGE_NAME: 查询指定包安装后生成的文件列表

⑤   rpm -qc PACEAGE_NEME：查询指定包安装的配置文件

⑥   rpm -qd PACKAGE_NAME: 查询指定包安装的帮助文件

⑦   rpm -q --scripts PACKAGE_NAME: 查询指定包中包含的脚本 

⑧   rpm -qf /path/to/somefile: 查询文件是由哪个rpm包安装生成的



### 3.安装jdk1.8

1. 到oracle官网 http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html；注册账号，下载jdk1.8 linux64位版，我的账号1563560161@qq.com，密码期刊密码

2. 用xftp6工具将安装包上传到虚拟机的`/opt/software/jdk/`路径下中

3. 安装jdk到`/usr/wjw`路径下，命令如下

   `rpm -ivh --prefix /usr/wjw {jdk的包名}`

4. 检查是否安装成功：`java -version`，若不报错，则安装成功