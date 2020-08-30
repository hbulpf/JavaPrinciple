## 观察者模式
### 介绍
```
定义对象间一种一对多的依赖关系，
当一个对象状态改变时，所有依赖对象都得到通知并自动更新。
```
### 设计
```
观察者和被观察者之间是一种抽象的耦合。需要：
Subject 抽象被观察者
Observer 抽象观察者
ConcreteSubject 具体的被观察者
ConcreteObserver 具体的观察者
```
### 应用场景
* 消息驱动的发布订阅/模型
* 文件系统中新建文件，通知目录管理器增加目录，通知磁盘管理器减少空间。文件是被观察者，目录管理器和磁盘管理器是观察者。
* 广播收音机：电台是被观察者，收音机是观察者。
* 老师讲课，老师是被观察者，学生是观察者。

###其他
- [自己实现观察者模式接口](../../src/main/dev/designpattern/observer/myObserver)
- [使用Java自带的观察者模式接口](../../src/main/dev/designpattern/observer/javaObserver)