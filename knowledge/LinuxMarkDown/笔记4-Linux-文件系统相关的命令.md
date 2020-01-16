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

5. `cd`命令

   ```shell 
   cd    # 切到管理员的家目录，即/root目录
   
   cd /    # 切到根目录
   
   cd ..    # 切到上一级目录
   
   pwd    # 查看当前目录，结果为：/root
   ```

6. `mkdir`命令：创建目录、文件夹

   ```shell
   mkdir wjw    # 在当前目录下创建wjw文件夹
   
   mkdir -p wjw/graduate/course    # 在当前目录下递归地创建文件夹,即有则不创建，没有则创建
   
   mkdir wjw/{pircture,musice,film} # 一次性创建多个文件
   ```

7. `cp`命令：拷贝

   ```shell
   ## cp source_dir target_dir
   cp /etc/profile /root/wjw    # 将profile文件拷贝到wjw目录下
   ```

8. `mv`命令：移动，重命名文件

   ```shell
   ## mv from_dir to_dir
   mv /root/wjw/profile /root/wjw/graduate/course    # 将profile文件移动到course
   mv /root/wjw/profile /root/wjw/profile.bak    # 将文件名重命名为profile.bak
   ```

   

### 难点：Linux中的软链接和硬链



（视频刷完后，回来再详细地看）