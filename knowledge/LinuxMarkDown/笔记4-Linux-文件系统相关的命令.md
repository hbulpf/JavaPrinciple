## Linux文件系统相关的命令

1. `df`命令：查看磁盘分区的使用情况

   ```SHELL
   df    # 查看磁盘分区的使用情况，单位为字节byte
   Filesystem     1K-blocks   Used Available Use% Mounted on
   /dev/sda3       59659396 847204  55781616   2% /
   tmpfs             502204      0    502204   0% /dev/shm
   /dev/sda1         198337  27798    160299  15% /boot
   
   df -h    # 查看磁盘分区的使用情况，有单位
   Filesystem      Size  Used Avail Use% Mounted on
   /dev/sda3        57G  828M   54G   2% /
   tmpfs           491M     0  491M   0% /dev/shm
   /dev/sda1       194M   28M  157M  15% /boot
   
   ```

2. `du`查看文件系统的使用情况

   ```SHELL
   cd /etc/sysconfig
   # 先使用ll命令查看有什么文件，再使用 du -h 文件名、文件夹名
   du -h networking
   4.0K	networking/devices
   4.0K	networking/profiles/default
   8.0K	networking/profiles
   16K	networking
   
   du -h sshd
   4.0K	sshd
   ```

3. `ls`显示当前目录的文件结构，`ls` +路径，显示该路径下的文件 

   ```shell
   ls \etc
   ```

4. `ll`命令，以列表的形式详细地显示当前目录的文件结构

   ```shell
   cd /etc/sysconfig
   -rw-r-----. 1 root root  647 Jun 22  2012 auditd
   -rw-r--r--. 1 root root  397 Jan  2 03:55 authconfig
   drwxr-xr-x. 2 root root 4096 Jan  2 03:54 cbq
   -rw-r--r--. 1 root root   21 Jan  2 03:55 clock
   ...
   
   #
   第1列是：文件类型，其中
   -：普通文件(f)
   d：目录文件，即文件夹
   b：块设备文件(block)
   c：字符设备文件(character)
   l：符号链接文件(symbolic link file)
   p：命令管道文件(pipe)
   s：套接字文件(socket)
   
   第2~10列是：文件权限，9位，每3位为一组，rwx(读，写，执行)，
       第一组表示的是属主(owner)的权限
       第二组表示的是属组(group,属组即owner所属的组)的权限
       第三组表示的是除了属组和属主以为的其他人的权限
   第13列是：文件硬链接的次数
   
   接下来的一次是：
   文件的属主(owner)
   文件的属组(group)
   文件大小(size)，单位是字节
   时间戳(timestamp)：最近一次被修改的时间
   #
   ```

   



