# java.exe和javaw.exe的区别

jdk的java.exe和javaw.exe文件都可以运行由javac.exe编译出来的java文件，但是，这两个程序都是有区别的：
1、java启动的程序是命令行程序或阻塞程序，如果该程序未执行完毕或未被关闭，则所打开的命令行将被阻塞，不能执行其它命令如dir等，可以通过Ctrl+C等方式关闭程序；
2、javaw启动的程序是窗口程序或非阻塞程序，在使用该命令运行程序后，可接着在命令行中执行下一命令，且启动的程序与命令行无关不依赖命令行，不能通过Ctrl+C关闭。

也可以这样解释：

两者的根本区别：java.exe是win32控制台应用,javaw.exe是一个win32的GUI应用

java.exe运行的程序之后cmd控制台进入阻塞状态，正在运行的cmd控制台不能输入其他命令，

而用javaw.exe运行java程序的话，控制台还可以输入其他命令。

java.exe调用的是系统System.console即系统控制台，javaw.exe调用的是java的GUI库，当用java.exe运行java用户界面的时候，实际上是java.exe通过系统的控制台调用java的GUI库，所以通过系统控制台输出GUI界面，所以cmd是出于阻塞状态的。

# 参考
1. java.exe和javaw.exe有什么区别吗 . https://blog.csdn.net/lmjsummer/article/details/79809214