# Maven 常用命令

## maven 命令的格式

maven 命令的格式为 `mvn [plugin-name]:[goal-name]` ，可以接受的参数如下:
```
-D 指定参数，如 -Dmaven.test.skip=true 跳过单元测试；
-P 指定 Profile 配置，可以用于区分环境；
-e 显示maven运行出错的信息；
-o 离线执行命令,即不去远程仓库更新包；
-X 显示maven允许的debug信息；
-U 强制去远程更新snapshot的插件或依赖，默认每天只更新一次。
```

## 常用命令
```
清理maven项目：mvn clean
验证项目是否正确以及必须的信息是否可用：mvn validate
编译源代码： mvn compile
测试编译后的代码，即执行单元测试代码：mvn test
maven 打包：mvn package
只打jar包：mvn jar:jar
检验package是否有效并且达到质量标准：mvn verify
编译测试代码：mvn test-compile
安装项目到本地仓库：mvn install
发布项目到远程仓库：mvn deploy

创建maven项目：mvn archetype:create
指定 group： -DgroupId=packageName
指定 artifact：-DartifactId=projectName
创建web项目：-DarchetypeArtifactId=maven-archetype-webapp
创建maven项目：mvn archetype:generate

生成源码jar包：mvn source:jar
产生应用需要的任何额外的源代码：mvn generate-sources

生成eclipse项目：mvn eclipse:eclipse
清理eclipse配置：mvn eclipse:clean
生成idea项目：mvn idea:idea

在集成测试可以运行的环境中处理和发布包：mvn integration-test
生成站点目录: mvn site
生成站点目录并发布：mvn site-deploy
安装本地jar到本地仓库：mvn install:install-file -DgroupId=packageName -DartifactId=projectName -Dversion=version -Dpackaging=jar -Dfile=path
显示maven依赖树：mvn dependency:tree
显示maven依赖列表：mvn dependency:list
下载依赖包的源码：mvn dependency:sources
查看实际pom信息: mvn help:effective-pom
分析项目的依赖信息：mvn dependency:analyze 或 mvn dependency:tree
查看帮助信息：mvn help:help 或 mvn help:help -Ddetail=true
查看插件的帮助信息：mvn :help，比如：mvn dependency:help 或 mvn ant:help
生成eclipse项目：mvn eclipse:eclipse
生成idea项目：mvn idea:idea
组合使用goal命令，如只打包不测试：mvn -Dtest package
只打jar包: mvn jar:jar
```

##  web项目相关命令
```
启动tomcat：mvn tomcat:run
启动jetty：mvn jetty:run
运行打包部署：mvn tomcat:deploy
撤销部署：mvn tomcat:undeploy
启动web应用：mvn tomcat:start
停止web应用：mvn tomcat:stop
重新部署：mvn tomcat:redeploy
部署展开的war文件：mvn war:exploded tomcat:exploded
```

##  maven执行顺序
```
mvn clean compile
mvn clean test
mvn clean package
mvn clean install
```
实际上，执行test之前会先执行compile的，执行package之前会先执行test，install之前会执行package.  
若直接以某一个phase为goal，将先执行完它之前的phase，如mvn install将会先validate、compile、test、package、integration-test、verify最后再执行install phase。

## 把项目部署到tomcat下的做法
```
在maven项目目录下： mvn tomcat:deploy
访问： http://localhost:8080/mycontext/ 即可。
撤销部署：mvn tomcat:undeploy
启动web应用：mvn tomcat:start
停止web应用：mvn tomcat:stop
重新部署：mvn tomcat:redeploy
部署展开的war文件：mvn war:exploded tomcat:exploded
```

## 其他命令
```
生成清除Eclipse项目结构：
mvn eclipse:eclipse
mvn eclipse:clean

清理（删除target目录下编译内容）:
mvn clean

仅打包Web页面文件:
mvn war:exploded

打包时跳过测试:
mvn package -Dmaven.test.skip=ture

跳过测试运行maven任务：
mvn -Dmaven.test.skip=true XXX

创建Maven的普通java项目(只适用于Maven2.x版本)：
mvn archetype:create -DgroupId=packageName -DartifactId=projectName

创建Maven的Web项目(只适用于Maven2.x版本)：
mvn archetype:create  -DgroupId=packageName-DartifactId=webappNameDarchetypeArtifactId=maven-archetype-webapp

创建Maven的Web项目(只适用于Maven3.x版本)：  
F:\SoftWare\Maven\MavenWebAppTest>mvn archetype:generate -DgroupId=com.shihuan -DartifactId=S3h3WebWs-DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false

创建Maven的Quickstart项目(只适用于Maven3.x版本)：
F:\SoftWare\Maven\MavenWebAppTest>mvn archetype:generate -DgroupId=com.shihuan -DartifactId=S3h3WebWs-DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

## 处理maven报错
```
清理maven未下载成功的jar包(在maven仓库下执行)
for /r %i in (*.lastUpdated) do del %i 
```
