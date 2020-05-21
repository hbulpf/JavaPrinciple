# Maven使用总结

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
mvn archetype:create -DgroupId=packageName -DartifactId=projectName

创建Maven的Web项目(只适用于Maven2.x版本)：
mvn archetype:create  -DgroupId=packageName-DartifactId=webappNameDarchetypeArtifactId=maven-archetype-webapp

创建Maven的Web项目(只适用于Maven3.x版本)：  
F:\SoftWare\Maven\MavenWebAppTest>mvn archetype:generate -DgroupId=com.shihuan -DartifactId=S3h3WebWs-DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false

创建Maven的Quickstart项目(只适用于Maven3.x版本)：
F:\SoftWare\Maven\MavenWebAppTest>mvn archetype:generate -DgroupId=com.shihuan -DartifactId=S3h3WebWs-DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

## 使用maven发布包

发布包有两种：安装到本地仓库 或者 发布到远程仓库

1. 安装到本地仓库

    ```
    mvn install:install-file -Dfile=[path to file] -DgroupId=[groupId] \
    -DartifactId=[artifactId] -Dversion=[version] -Dpackaging=jar 
    ```

    例子：

    ```
    mvn install:install-file -Dfile=activiti-engine-5.12.jar  -DgroupId=org.activiti \
    -DartifactId=activiti-engine  -Dversion=5.12 -Dpackaging=jar -DgeneratePom=true
    ```

2. 发布到远程仓库

    ```
    mvn deploy:deploy-file -Dfile=[path to file] -DgroupId=[groupId] -DartifactId=[artifactId] \
    -Dversion=[version] -Dpackaging=jar -DgeneratePom=false -Durl=[url] -DrepositoryId=[id] \
    -s [path of settings.xml]
    ```
    例子1：

    ```
    mvn deploy:deploy-file -Dfile=D:\fastjson-1.1.2.jar -DgroupId=com.alibaba \
    -DartifactId=fastjson -Dversion=1.1.2 -Dpackaging=jar -DgeneratePom=false \
    -Durl=http://192.168.1.222:8081/nexus/content/repositories/releases -DrepositoryId=releases \
    -s settings.xml
    ```

    例子2:

    ```
    mvn deploy:deploy-file -Dfile=ojdbc.jar -DgroupId=com.oracle \
    -DartifactId=ojdbc14 -Dversion=10.2.0.4.0 -Dpackaging=jar -DgeneratePom=false \
    -Durl=http://localhost:8081/nexus/content/repositories/thirdparty  -DrepositoryId=thirdparty \
    -s settings.xml
    ```

    需要配置setting.xml中server。用户名密码是远程仓库的用户名/密码，就是创建的nexus的仓库登陆用户名密码

## 处理maven报错
```
清理maven未下载成功的jar包(在maven仓库下执行)
for /r %i in (*.lastUpdated) do del %i 
```

## dependency的说明

#### dependency的type

引入某一个依赖时，必须指定type，这是因为用于匹配dependency引用和dependencyManagement部分的最小信息集实际上是{groupId，artifactId，type，classifier}。

在很多情况下，这些依赖关系将引用没有classifier的jar依赖。这允许我们将标识设置为{groupId，artifactId}，因为type的默认值是jar，并且默认classifier为null。

type的值一般有jar、war、pom等，声明引入的依赖的类型

#### dependency的classifier

Classifier可能是最容易被忽略的Maven特性，但它确实非常重要，我们也需要它来帮助规划坐标。设想这样一个情况，有一个jar项目，就说是 dog-cli-1.0.jar 吧，运行它用户就能在命令行上画一只小狗出来。现在用户的要求是希望你能提供一个zip包，里面不仅包含这个可运行的jar，还得包含源代码和文档，换句话说，这是比较正式的分发包。这个文件名应该是怎样的呢？dog-cli-1.0.zip？不够清楚，仅仅从扩展名很难分辨什么是Maven默认生成的构件，什么是额外配置生成分发包。如果能是dog-cli-1.0-dist.zip就最好了。这里的dist就是classifier，默认Maven只生成一个构件，我们称之为主构件，那当我们希望Maven生成其他附属构件的时候，就能用上classifier。常见的classifier还有如dog-cli-1.0-sources.jar表示源码包，dog-cli-1.0-javadoc.jar表示JavaDoc包等等。

classifier它表示在相同版本下针对不同的环境或者jdk使用的jar,如果配置了这个元素，则会将这个元素名在加在最后来查找相应的jar，例如：

```
<classifier>jdk17</classifier>
<classifier>jdk18</classifier>
```


#### dependencyManagement

在Maven中dependencyManagement的作用其实相当于一个对所依赖jar包进行版本管理的管理器。

pom.xml文件中，jar的版本判断的两种途径

1. 如果dependencies里的dependency自己没有声明version元素，那么maven就会到dependencyManagement里面去找有没有对该artifactId和groupId进行过版本声明，如果有，就继承它，如果没有就会报错，告诉你必须为dependency声明一个version

2. 如果dependencies中的dependency声明了version，那么无论dependencyManagement中有无对该jar的version声明，都以dependency里的version为准。

    ```
    //只是对版本进行管理，不会实际引入jar  
    <dependencyManagement>  
        <dependencies>  
                <dependency>  
                    <groupId>org.springframework</groupId>  
                    <artifactId>spring-core</artifactId>  
                    <version>3.2.7</version>  
                </dependency>  
        </dependencies>  
    </dependencyManagement>  
    
    //会实际下载jar包  
    <dependencies>  
        <dependency>  
                    <groupId>org.springframework</groupId>  
                    <artifactId>spring-core</artifactId>  
        </dependency>  
    </dependencies>
    ```


## Scope的属性说明

scope定义了类包在项目的使用阶段。项目阶段包括： 编译，运行，测试和发布。

- **compile**: 默认scope为compile，表示为当前依赖参与项目的编译、测试和运行阶段，属于强依赖。打包之时，会达到包里去。
- **test**: 该依赖仅仅参与测试相关的内容，包括测试用例的编译和执行，比如定性的Junit。
- **runtime**: 依赖仅参与运行周期中的使用。一般这种类库都是接口与实现相分离的类库，比如JDBC类库，在编译之时仅依赖相关的接口，在具体的运行之时，才需要具体的mysql、oracle等等数据的驱动程序。
此类的驱动都是为runtime的类库。
- **provided**: 该依赖在打包过程中，不需要打进去，这个由运行的环境来提供，比如tomcat或者基础类库等等，事实上，该依赖可以参与编译、测试和运行等周期，与compile等同。区别在于打包阶段进行了exclude操作。
- **system**: 使用上与provided相同，不同之处在于该依赖不从maven仓库中提取，而是从本地文件系统中提取，其会参照systemPath的属性进行提取依赖。
- **import** : 这个是maven2.0.9版本后出的属性，import只能在dependencyManagement的中使用，能解决maven单继承问题，import依赖关系实际上并不参与限制依赖关系的传递性。

### maven 引入本地jar包

> 当maven依赖本地而非repository中的jar包，sytemPath指明本地jar包路径。


1. 在pom.xml同级目录下新建lib文件夹，并放入本地jar包。
2. 配置Jar包的dependency，包括groupId，artifactId，version三个属性，同时还要包含scope和systemPath属性，分别指定Jar包来源于本地文件，和本地文件的所在路径。

    ```
    <dependency>
        <groupid>org.hamcrest</groupid>
        <artifactid>hamcrest-core</artifactid>
        <version>1.5</version>
        <scope>system</scope>
        <systempath>${basedir}/WebContent/WEB-INF/lib/hamcrest-core-1.3.jar</systempath>
    </dependency>
    ```

    如果是多模块项目，<systempath> 可以用当前 pom.xml 的方式导入本地jar包。

    POM文件里面可以引用一些内置属性(Maven预定义可以直接使用)  

    > ${basedir} 项目根目录   
    > ${version}表示项目版本;  
    > ${project.basedir}同${basedir};  
    > ${project.version}表示项目版本,与${version}相同;  
    > ${project.build.directory} 构建目录，缺省为target  
    > ${project.build.sourceEncoding}表示主源码的编码格式;  
    > ${project.build.sourceDirectory}表示主源码路径;  
    > ${project.build.finalName}表示输出文件名称;  
    > ${project.build.outputDirectory} 构建过程输出目录，缺省为target/classes  


3. 配置插件将本地jar包打入运行jar/war包中，由于scope=system,默认并不会将Jar包打进jar/war包中，所以需要通过插件进行打包。

    ```
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-dependency-plugin</artifactId>
        <version>2.10</version>
        <executions>
            <execution>
                <id>copy-dependencies</id>
                <phase>compile</phase>
                <goals>
                    <goal>copy-dependencies</goal>
                </goals>
                <configuration>
                    <outputDirectory>${project.build.directory}/${project.build.finalName}/WEB-INF/lib</outputDirectory>
                    <includeScope>system</includeScope>
                </configuration>
            </execution>
        </executions>
    </plugin>
    ```
    
    或者在项目根目录下运行，通过mvn install:install-file命令安装lib目录中的jar包到本地Maven仓库。
    ```
    mvn install:install-file -DgroupId=com.aliyun.mns -DartifactId=aliyun-sdk-mns -Dversion=1.1.8 -Dfile=lib/aliyun-sdk-mns-1.1.8.jar -Dpackaging=jar -DgeneratePom=true
    ```
    然后在项目POM.xml文件中引用。
    ```
    <dependency>
        <groupId>com.aliyun.mns</groupId>
        <artifactId>aliyun-sdk-mns</artifactId>
        <version>1.1.8</version>
    </dependency>
    ```

4. 将依赖jar包打包至jar包中

    方法一：

    ```
    <build>
            <finalName>包名</finalName>
            <plugins>
                <!--源码编译-->
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.7.0</version>
                    <configuration>
                        <source>1.8</source>
                        <target>1.8</target>
                    </configuration>
                </plugin>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-assembly-plugin</artifactId>
                    <version>2.4.1</version>
                    <configuration>
                        <appendAssemblyId>false</appendAssemblyId>
                        <descriptorRefs>
                            <descriptorRef>jar-with-dependencies</descriptorRef>
                        </descriptorRefs>
                        <archive>
                            <manifest>
                                <mainClass>包程序主类</mainClass>
                            </manifest>
                        </archive>
                    </configuration>
                    <executions>
                        <execution>
                            <id>make-assembly</id>
                            <phase>package</phase>
                            <goals>
                                <goal>assembly</goal>
                            </goals>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
    ```

    方法二：生成含依赖xxx.jar包和original-xxx.jar不含依赖jar包。

    ```
    <build>
            <finalName>包名</finalName>
            <plugins>
                <!--源码编译-->
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.7.0</version>
                    <configuration>
                        <source>1.8</source>
                        <target>1.8</target>
                    </configuration>
                </plugin>
                <!-- shade插件打包成jar包 -->
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-shade-plugin</artifactId>
                    <version>3.1.1</version>
                    <executions>
                        <execution>
                            <phase>package</phase>
                            <goals>
                                <goal>shade</goal>
                            </goals>
                            <configuration>
                                <transformers>
                                    <transformer
            implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                        <mainClass>包程序主类</mainClass>
                                    </transformer>
                                </transformers>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>         
            </plugins>
        </build>
    ```

# 参考

1. [Maven的Scope区别笔记](https://blog.csdn.net/blueheart20/article/details/81014116) . https://blog.csdn.net/blueheart20/article/details/81014116
1. [maven pom 引入本地jar包](https://www.cnblogs.com/lenovo_tiger_love/archive/2018/10/29/9873755.html) . https://www.cnblogs.com/lenovo_tiger_love/archive/2018/10/29/9873755.html
1. [Maven引入本地Jar包](https://www.jianshu.com/p/f50841f0963d) . https://www.jianshu.com/p/f50841f0963d