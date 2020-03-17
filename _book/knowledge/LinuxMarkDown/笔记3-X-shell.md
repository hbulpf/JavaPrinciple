## X-Shell命令

### 1. 安装X-shell

1. 安装x-shell, 安装x-ftp工具

2. x-shell工具的作用是连接主机（虚拟机），操作linux系统；x-ftp的作用是用于虚拟机与本地的文件传输

3. 如何连接主机？

   在x-shell中创建一个会话，[具体流程](https://jingyan.baidu.com/article/39810a235e5b3eb636fda691.html)，关键是要输入用户名、密码和主机的ip

   用户名：root

   密码：登陆虚拟机的密码

   ip：在虚拟机中执行`ifconfig`命令查看ip，注意键值对`inet addr:192.xxx.xx.xx.xx`，其中`192.xxx.xx.xx.xx`就是ip

   ![image-20200108000258658](https://github.com/wangjiawen1993year/JavaPrinciple/blob/master/knowledge/resource/picture/image-20200110213606607.png)



### 2. 什么是shell

shell是操作linux系统的一类软件、程序，现在常用的shell的bash shell，它安装在linux系统上的。用户输入正确的用户名和密码后，才能进入bash shell对操作linux系统。方式是，通过命令来进行操作，具体过程如下图。

![image-20200108224935217](https://github.com/wangjiawen1993year/JavaPrinciple/blob/master/knowledge/image-20200108224935217.png)

1. 在shell中输入命令后，则交给linux内核来执行命令。在shell的命令行中输入字符串后，用字符串中的第一个空格为分隔符，空格前的是命令，空格后的是命令的参数。若字符串中没有空格，则认为整个字符串是一个不带参数的命令。
2. 命令分为两类，分别是内部命令和外部命令。其中内部命令是shell自带的，而外部命令则是用户自己安装的命令（或可执行文件）。需注意的是，对于外部命令，它得路径只有配置在环境变量`path`中的时候才能找到。
3. 执行外部命令，效率会很低吗？--不会，因为系统只会在path中配置的路径里寻找是否存在输入的命令，而不会全局地寻找，因此速度不会慢。

可以使用`echo`命令查看环境变量`path`的值

`echo`命令：打印命令，相当于java里的`System.out.println()`

``` shell
echo $path
# 输出path的值

echo "123"
# 123
```



### 3. X-shell命令

命令分为两种：内部命令（shell自带的命令）和外部命令（不是shell自带的命令，由用户自己安装的命令），

1. `type` 命令：查看命令的类型，内部命令还是外部命令，例如

```shell
type cd
cd is a shell builtin  # cd是shell自带的命令，即内部命令 

type ifconfig
# ifconfig is /sbin/ifconfig  # ifconfig可执行文件的路径是/sbin/ifconfig，即外部命令
```



2. `file`命令：查看文件类型

``` shell
cd /sbin   # 切到ifconfig的目录下
file ifconfig
ifconfig: ELF 64-bit LSB executable, x86-64, version 1 (SYSV), dynamically linked # (uses shared libs), for GNU/Linux 2.6.18, stripped
```



3. `whereis`命令：查看文件的路径

```shell
whereis ifconfig
# ifconfig: /sbin/ifconfig /usr/share/man/man8/ifconfig.8.gz
```



4. `cat`命令：查看文件内容（以文本的方式打开）

```shell
cat ifconfig     # 结果会出现乱码，因为可执行文件以文本方式打开会乱码

cd /etc/sysconfig/network-scripts/
cat ifcfg-eth0
# 执行结果如下：
DEVICE=eth0
TYPE=Ethernet
ONBOOT=yes
NM_CONTROLLED=yes
BOOTPROTO=static
IPADDR=192.168.65.201
NETMASK=255.255.255.0
GATEWAY=192.168.65.28
DNS1=114.114.114.114
DNS2=192.168.65.28
```



5. `man`命令：manual的缩写，参考命令的说明文档

```shell
man ifconfig   # 查看ifconfig命令的说明文档
```

若系统提示没有`man`命令，则使`yum`命令安装`man`，`yum`命令是包管理器

```shell
yum install man # 安装man可执行文件
```

对于内部命令，也可以使用`help `来查看

``` shell
help cd
```



6. 进程相关的命令，`ps`命令

```shell
ps -ef    # 打印当前bash shell中的所有进程，如下图所示，其中PID表示进程号
```

![image-20200110213606607](https://github.com/wangjiawen1993year/JavaPrinciple/blob/master/knowledge/image-20200110213606607.png)

这个进程组成的清单类似于windows系统中的任务管理里进程清单。

强制关闭进程命令，使用`kill`命令，类似于windows里用任务管理器结束任务

```shell
# kill -9 {进程号}，例如
kill -9 18    # 强制关闭进程号为18的进程
```



7. shell用哈希表优化搜索时间的原理：shell对于已经执行过的命令，会将该命令的路径暂时缓存在一个hash表中（key为命令名，value为该命令的路径），这样在下次执行命令时，shell会先在hash表中查找该命令的路径，若找不到，则再去在环境变量PATH中寻找。因此，对于已执行过的命令就能够更快地找到，而不必每次都去环境变量PATH中搜索。用`hash`命令可以打印出当前hash表中缓存的路径。

```shell
hash
####输出结果
hits	command
   1	/sbin/ifconfig
   1	/usr/bin/man
   2	/bin/ls
####
```



### 4. 变量

定义变量

```shell
a=1    # 注意定义变量时，不能有空格，即不能写成 a = 1 这种形式
echo $a    # 打印变量,用美元符号“$”+变量名a，输出结果为1

b=abc
echo $b    # 输出结果为abc
```



定义数组

```shell

a=456
arr=(1 2 3 ab $a)    # 用括号括住所有元素，每个元素用空格分隔开，
echo ${arr[0]}    # 打印数组中第0个元素，输出结果为1
echo ${arr[3]}    # 打印数组中的第3个元素，输出结果ab
echo ${arr[4]}    # 打印数组中的第4个元素，输出结果为456
```

















