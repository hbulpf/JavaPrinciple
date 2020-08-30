# Java的classpath指什么

> 开发项目时只知道把配置文件如：mybatis.xml、spring-web.xml、applicationContext.xml等放到src目录（就是存放代码.java文件的目录），然后使用“classpath：xxx.xml”来读取，都放到src目录准没错，那么到底classpath指的是什么位置呢？ 


## classpath路径指在哪里

src路径下的文件在编译后会放到WEB-INF/classes路径下吧。默认的classpath是在这里。直接放到WEB-INF下的话，是不在classpath下的。用ClassPathXmlApplicationContext当然获取不到。  
如果单元测试的话，可以在启动或者运行的选项里指定classpath的路径的。  

用maven构建项目时候resource目录就是默认的classpath  
classPath即为java文件编译之后的class文件的编译目录一般为web-inf/classes，src下的xml在编译时也会复制到classPath下 
```
ApplicationContext ctx = new ClassPathXmlApplicationContext("xxxx.xml");  //读取classPath下的spring.xml配置文件  
ApplicationContext ctx = new FileSystemXmlApplicationContext("WebRoot/WEB-INF/xxxx.xml");   //读取WEB-INF 下的spring.xml文件 
```

## web.xml 配置中classpath: 与classpath*:的区别

首先 classpath是指 WEB-INF文件夹下的classes目录.解释下classes:

1. 存放各种资源配置文件 eg.init.properties log4j.properties struts.xml   
2. 存放模板文件 eg.actionerror.ftl   
3. 存放class文件 对应的是项目开发时的src目录编译文件   

总结：这是一个定位资源的入口  

`classpath` 和 `classpath*` 区别：

- `classpath` ：只会到class路径中查找文件; 
    ```
    <param-value>classpath:applicationContext-*.xml</param-value>  
    或者引用其子目录下的文件,如 
    <param-value>classpath:context/conf/controller.xml</param-value>  
    ```
- `classpath*` ：不仅包含class路径，还会在jar文件中(class路径)进行查找.   
  当项目中有多个classpath路径，并同时加载多个classpath路径下（此种情况多数不会遇到）的文件，*就发挥了作用，如果不加*，则表示仅仅加载第一个classpath路径
    ```
    <param-value>classpath*:context/conf/controller*.xml</param-value>  
    ```

总结:

`classpath` 只会从第一个classpath中加载，而 `classpath*` 会从所有的classpath中加载。

如果要加载的资源，不在当前ClassLoader的路径里，那么用 `classpath:` 前缀是找不到的，这种情况下就需要使用 `classpath*:` 前缀。

在多个classpath中存在同名资源，都需要加载，那么用 `classpath:` 只会加载第一个，这种情况下也需要用 `classpath*:` 前缀 


另外： 

`**/`  表示的是任意目录； 
`**/applicationContext-*.xml`  表示任意目录下的以 `applicationContext-` 开头的XML文件。  
程序部署到tomcat后，src目录下的配置文件会和class文件一样，自动copy到应用的 WEB-INF/classes目录下.





