## Linux虚拟机

### 一. 登录

login: root

password: xxxxxxx

### 二. 配置虚拟环境网络

#### 1. 找到网卡

到达如下的路径

> `cd /etc/sysconfig/network-scripts/`

查看该目录下的文件

> `ls`

注意以下文件：ifcfg-eth0，其中if表示interface，cfg是config缩写，eth是ethernet（以太网）的缩写，ifcfg-eth0表示第0块网卡。

#### 2. 配置协议

vi命令可以理解为文本编辑器，用文本编辑器打开ifclg-eth0文件（注意，使用`vi`命令打开文件时，若文件不存在时，则会创建这个文件）

> `vi ifclg-eth0` 

打开的文件如下：

> DEVICE=eth0
>
> HWADDR=00:0C:29:50:BC:A4
>
> TYPE=Ethernet
>
> UUID=XXXXX
>
> ONBOOT=no
>
> NM_CONTROLLED=yes
>
> BOOTRPOTO=dhcp



按下键盘的 “i” 键，进入编辑状态，并把上述文件修改如下：

>DEVICE=eth0
>
>#HWADDR=00:0C:29:50:BC:A4，注释掉
>
>TYPE=Ethernet
>
>#UUID=XXXXX， 注释掉
>
>ONBOOT=yes    # 改为yes
>
>NM_CONTROLLED=yes
>
>BOOTRPOTO=static    # 改为static
>
>IPADDR=192.168.65.66  # IP地址
>
>NETMASK=255.255.255.0  # 掩码
>
>GATEWAY=192.168.65.2   # 网关
>
>DNS1=114.114.114.114
>
>DNS2=192.168.65.2

按下Esc键，并输入`:wq`，再按下回车保存文件并退出，回到控制台



#### 4. 重启网络服务

重启网络服务

`service network restart`

查看配置结果

`ifconfig`

测试一下能否ping通百度网

`ping www.baidu.com`







