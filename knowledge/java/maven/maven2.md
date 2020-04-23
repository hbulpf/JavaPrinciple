# Maven 扩展知识

## Scope属性

scope定义了类包在项目的使用阶段。项目阶段包括： 编译，运行，测试和发布。

#### compile

默认scope为compile，表示为当前依赖参与项目的编译、测试和运行阶段，属于强依赖。打包之时，会达到包里去。

#### test

该依赖仅仅参与测试相关的内容，包括测试用例的编译和执行，比如定性的Junit。

#### runtime

依赖仅参与运行周期中的使用。一般这种类库都是接口与实现相分离的类库，比如JDBC类库，在编译之时仅依赖相关的接口，在具体的运行之时，才需要具体的mysql、oracle等等数据的驱动程序。
此类的驱动都是为runtime的类库。

#### provided

该依赖在打包过程中，不需要打进去，这个由运行的环境来提供，比如tomcat或者基础类库等等，事实上，该依赖可以参与编译、测试和运行等周期，与compile等同。区别在于打包阶段进行了exclude操作。

#### system

使用上与provided相同，不同之处在于该依赖不从maven仓库中提取，而是从本地文件系统中提取，其会参照systemPath的属性进行提取依赖。

#### import

这个是maven2.0.9版本后出的属性，import只能在dependencyManagement的中使用，能解决maven单继承问题，import依赖关系实际上并不参与限制依赖关系的传递性。

#### systemPath

当maven依赖本地而非repository中的jar包，sytemPath指明本地jar包路径,例如：

```
<dependency>
    <groupid>org.hamcrest</groupid>
    <artifactid>hamcrest-core</artifactid>
    <version>1.5</version>
    <scope>system</scope>
    <systempath>${basedir}/WebContent/WEB-INF/lib/hamcrest-core-1.3.jar</systempath>
</dependency>
```



#### dependency中的type

引入某一个依赖时，必须指定type，这是因为用于匹配dependency引用和dependencyManagement部分的最小信息集实际上是{groupId，artifactId，type，classifier}。在很多情况下，这些依赖关系将引用没有classifier的jar依赖。这允许我们将标识设置为{groupId，artifactId}，因为type的默认值是jar，并且默认classifier为null。
type的值一般有jar、war、pom等，声明引入的依赖的类型

dependency中的type
引入某一个依赖时，必须指定type，这是因为用于匹配dependency引用和dependencyManagement部分的最小信息集实际上是{groupId，artifactId，type，classifier}。在很多情况下，这些依赖关系将引用没有classifier的jar依赖。这允许我们将标识设置为{groupId，artifactId}，因为type的默认值是jar，并且默认classifier为null。
type的值一般有jar、war、pom等，声明引入的依赖的类型

#### dependency中的classifier

Classifier可能是最容易被忽略的Maven特性，但它确实非常重要，我们也需要它来帮助规划坐标。设想这样一个情况，有一个jar项目，就说是 dog-cli-1.0.jar 吧，运行它用户就能在命令行上画一只小狗出来。现在用户的要求是希望你能提供一个zip包，里面不仅包含这个可运行的jar，还得包含源代码和文档，换句话说，这是比较正式的分发包。这个文件名应该是怎样的呢？dog-cli-1.0.zip？不够清楚，仅仅从扩展名很难分辨什么是Maven默认生成的构件，什么是额外配置生成分发包。如果能是dog-cli-1.0-dist.zip就最好了。这里的dist就是classifier，默认Maven只生成一个构件，我们称之为主构件，那当我们希望Maven生成其他附属构件的时候，就能用上classifier。常见的classifier还有如dog-cli-1.0-sources.jar表示源码包，dog-cli-1.0-javadoc.jar表示JavaDoc包等等。

classifier它表示在相同版本下针对不同的环境或者jdk使用的jar,如果配置了这个元素，则会将这个元素名在加在最后来查找相应的jar，例如：

```
<classifier>jdk17</classifier>
<classifier>jdk18</classifier>
```

## dependencyManagement

在Maven中dependencyManagement的作用其实相当于一个对所依赖jar包进行版本管理的管理器。

pom.xml文件中，jar的版本判断的两种途径

1. 如果dependencies里的dependency自己没有声明version元素，那么maven就会倒dependencyManagement里面去找有没有对该artifactId和groupId进行过版本声明，如果有，就继承它，如果没有就会报错，告诉你必须为dependency声明一个version

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
