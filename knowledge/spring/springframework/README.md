# Spring 框架


![img](imgs/1338117197_8461.gif)

## 核心容器(Spring core)

核心容器提供Spring框架的基本功能。Spring以bean的方式组织和管理Java应用中的各个组件及其关系。Spring使用BeanFactory来产生和管理Bean，它是工厂模式的实现。

BeanFactory使用控制反转(IoC)模式将应用的配置和依赖性规范与实际的应用程序代码分开。BeanFactory使用依赖注入的方式提供给组件依赖。

## Spring上下文(Spring context)

Spring上下文是一个配置文件，向Spring框架提供上下文信息。

Spring上下文包括企业服务，如JNDI(Java命名和目录接口)、EJB(Enterprise Java Beans称为Java 企业Bean)、电子邮件、国际化、校验和调度功能。


## Spring Web模块

Web上下文模块建立在应用程序上下文模块之上，为基于Web的应用程序提供了上下文。Web层使用Web层框架，可选的，可以是Spring自己的MVC框架，或者提供的Web框架，如Struts、Webwork、tapestry和jsf。

## Spring面向切面编程(Spring AOP)

通过配置管理特性，Spring AOP 模块直接将面向方面的编程功能集成到了 Spring框架中。可以很容易地使 Spring框架管理的任何对象支持 AOP。

Spring AOP 模块为基于 Spring 的应用程序中的对象提供了事务管理服务。通过使用 Spring AOP，不用依赖 EJB 组件，就可以将声明性事务管理集成到应用程序中。

## Spring DAO模块

DAO模式主要目的是将持久层相关问题与一般的的业务规则和工作流隔离开来。

Spring 中的DAO提供一致的方式访问数据库，不管采用何种持久化技术，Spring都提供一致的编程模型。

Spring对不同的持久层技术提供一致的DAO方式的异常层次结构，可用该结构来管理异常处理和不同数据库供应商抛出的错误消息。异常层次结构简化了错误处理，并且极大地降低了需要编写的异常代码数量（例如打开和关闭连接）。Spring DAO 面向 JDBC 的异常遵从通用的 DAO 异常层次结构。

## Spring ORM模块

Spring 框架兼容了若干个 ORM 框架，从而提供了 ORM 的对象关系工具，其中包括 Hibernate、JDO实现、TopLink和IBatis SQL Map。所有这些都遵从 Spring 的通用事务和 DAO 异常层次结构。Spring为所有的这些框架提供了模板之类的辅助类，形成了一致的编程风格。


## Spring MVC框架(Spring WebMVC)

MVC框架是一个全功能的构建Web应用程序的MVC实现。通过策略接口，MVC框架变成为高度可配置的，MVC 容纳了大量视图技术，其中包括 JSP。。Spring的MVC框架提供清晰的角色划分：控制器、验证器、命令对象、表单对象和模型对象、分发器、处理器映射和视图解析器。Spring支持多种视图技术。  



## 学习

1. [Spring.IO官方文档](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-spring-application)