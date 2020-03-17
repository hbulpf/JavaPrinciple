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
   
   
   ## 拷贝目录，加上 -r 参数，cp -r {FROM} {TO}
   cp -r /root/wjw/graduate /root/wjw/film
   ```

8. `mv`命令：移动，重命名文件

   ```shell
   ## mv from_dir to_dir
   mv /root/wjw/profile /root/wjw/graduate/course    # 将profile文件移动到course
   mv /root/wjw/profile /root/wjw/profile.bak    # 将文件名重命名为profile.bak
   ```

9. `stat`命令：查看文件的详细信息（比`ls`，`ll`命令更详细）

   ```shell
   stat /root/wjw/graduate/course/profile
   #### 结果如下
     File: `/root/wjw/graduate/course/profile'
     Size: 1893      	Blocks: 8          IO Block: 4096   regular file
   Device: 803h/2051d	Inode: 661677      Links: 1
   Access: (0644/-rw-r--r--)  Uid: (    0/    root)   Gid: (    0/    root)
   Access: 2020-01-26 21:35:03.883112767 +0800
   Modify: 2020-01-26 21:35:03.883112767 +0800
   Change: 2020-01-26 21:35:54.352107884 +0800
   ####
   
   # Access是上次的访问时间
   # Modify是文件内容上次的修改时间
   # Change是上次文件元数据的修改时间，文件元数据是用于描述文件的一类数据，如文件的权限，创建时间，位置，大小，文件名称等
   ```

10. `touch`命令：将文件的Access，Modify，Change命令设置为当前时间戳；使用`touch`命令也可以创建新文件

    ```shell
    # date命令，可以打印当前的时间戳
    date
    # Sun Jan 26 22:57:28 CST 2020
    touch profile
    
    # 使用stat命令查看结果
    stat profile
    ####
      File: `profile'
      Size: 1893      	Blocks: 8          IO Block: 4096   regular file
    Device: 803h/2051d	Inode: 661677      Links: 1
    Access: (0644/-rw-r--r--)  Uid: (    0/    root)   Gid: (    0/    root)
    Access: 2020-01-26 22:57:33.920106988 +0800
    Modify: 2020-01-26 22:57:33.920106988 +0800
    Change: 2020-01-26 22:57:33.920106988 +0800
    ####
    
    #### 用touch命令，创建文件
    touch newText
    ```

11. 文本系统相关的命令

    ```shell
    cat profile    #### 查看文件内容，一次性将整个文本文件的内容打印出来
    
    more profile   #### 分页显示，按空格键显示下一页，按回车显示下一行，按键盘的 q 键退出
    
    less profile   #### 分页显示，但一次性把整个内存都读到内存中，因此，可以向前和向后翻页
    
    head -5 profile    #### 查看前5行的数据
    
    tail -5 profile    #### 查看后5行的数据
    
    tail -f profile    #### 输出增量数据
    #### 测试 tail -f 命令
    # 用另一个进程/shell窗口，echo "123" >> profile 往profile文件中写数据
    # 然后就能实时地打印出 123
    ```

12. 管道：{命令1} | {命令2}，将命令1的输出作为命令2的输入，最终的输出结果为命令2的输出

    ```shell
    cat profile | head -5    #### 查看文件的前5行数据
    
    # 使用xargs命令
    echo "/" | xargs ls -l    #### 效果相当于 ls -l /
    
    # 打印profile第6行数据
    head -6 profile | tail -1
    ```




### vi文本编辑器模式

三种模式：编辑模式，输入模式，末行模式。

1. **编辑模式**：用命令`vi {文件名}`进入编辑模式，在编辑模式下，可执行的操作：

   *① 行内*：

   0：光标移动到绝对行首

   ^：光标移动到行首的第一个非空白字符

   $：光标移动到绝对行尾

   *② 行间*：

   G：光标移动到文章末尾

   gg：光标移动到文章开头

   3G：光标移动到第3行

   *③ 翻屏*：

   ctrl+ f，b：向前，向后翻屏

   *④ 删除，替换单个字符*：

   x：删除光标位置字符

   3x：删除光标开始3个字符

   r：替换光标位置字符

   *⑤ 删除命令*：

   dd：删除光标所在的行

   dw：删除光标所在的单词

   *⑥ 复制粘贴、剪切*：

   yw：复制光标所在的单词

   yy：复制光标所在的行

   p：将光标移动到想要粘贴的位置后，按p键将复制的内容粘贴下来

   *⑥ 撤销、重做*：

   u 撤销

   ctrl + r：恢复撤销



2. **输入模式**：用命令`vi {文件名}`进入编辑模式，再按键盘的i键进入输入模式（相当于windows系统中用记事本打开了文件），输入完后按键盘的ESC键返回编辑模式；



3. **末行模式**：在编辑模式下，按键盘的冒号键 ” : “ 进入末行模式

   *① 保存退出*：

   q：退出，没有动过文件；若有编辑过文件，则此命令报错。

   wq：保存并退出，动过了，不后悔

   q!：不保存并退出，动过了，后悔了

   w：保存

   w!：强行保存，对于只读模式的文件也能保存修改

   *② set命令*：

   set nu 或 set number：显示行号

   set nonu 或 set nonumber：隐藏行号

   set readonly：设置为只读模式

   ③ *查找*：

   /{target_char}：查找字符串，按n键则往下查找，按N键则往上查找

   *④ 查找并替换*：

   s/after/before：将光标所在行的第一个after替换为before

   s/after/before/g：将光标所在行的所有after替换为before

   s/after/before/gi：将光标所在行的所有after替换为before，忽略大小写

   %s/after/before/gi：将全文的before替换为after

   0,$s/after/before/gi：将第0行到最后一行的after替换为before

   *⑤ 删除*：

   0,$d 或 %d：删除全文

   *⑥ 复制粘贴*：

   1,3y：复制第一行到第3行的内容

   p：将光标移动到想要粘贴的位置，按p键粘贴下来







### 难点：

### 1.Linux中的软链接和硬链

### 2.管道的用法，xargs命令



（视频刷完后，回来再详细地看）

| 操作符                 | 作用                                         |
| ---------------------- | :------------------------------------------- |
| \                      | 转义字符                                     |
| .                      | 匹配任意单个字符                             |
| [1249a], [\^12], [a-k] | 字符序列单字符占位                           |
| ^                      | 行首                                         |
| $                      | 行尾                                         |
| \\<，\\>               | 单词首尾边界，例如\\<hello\\>表示hello单词｜ |
| ｜                     | 连接操作符                                   |
| (,)                    | 选择操作符                                   |
| \\n                    | 反向引用                                     |