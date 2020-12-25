# Tomcat

更多 tomcat 见专题系列: https://gitee.com/RunAtWorld/tomcatdemo/tree/master/docs

## windows 下 tomcat后台运行的方法

我们使用tomcat 启动一个新的项目，会出现一个窗口。如果有人需要使用这台电脑的时候，就很容易关掉tomcat 导致程序停止运行。为避免这种错误把tomcat设置为后台运行。

下面两种方法实现tomcat 后台运行；

一、修改tomcat里面的配置

1. 找到tomcat下bin/setclasspath.bat文件,右键EditPlus打开。
2. 在文件中找到 set_RUNJAVA="%JRE_HOME\bin\java", 并修改为set_RUNJAVA="%JRE_HOME\bin\javaw" 。
3. 重启tomcat，命令行窗口即会消失，不会出现在任务栏上，而只是在后台运行。

二、把tomcat 注册成服务，使用命令方式（推荐）

这种方式配置可以用程序来控制tomcat的启停，无需手动来控制启停。

1. 进入Tomcat  `D:\server\tomcat\apache-tomcat-8.5.15\bin`  目录 找到 `service.bat` 的文件。
（有的tomcat bin 目录下没有 service.bat文件，可以重新下载tomcat安装包）

2. 以管理员身份运行 cmd 进入 `D:\server\tomcat\apache-tomcat-8.5.15\bin` 
弹出窗口输入 
```
service.bat install
```       
按下Enter键出现下面内容证明成功
      
在打开计算机—>管理->服务这一栏可以看到在服务中添加了一个tomcat的服务，只需要将此服务开启即可，若要开机启动就将服务设成是自动的。

 
 # 参考
 
 1. [tomcat后台运行的两种方法](https://www.cnblogs.com/citime/p/10062289.html)