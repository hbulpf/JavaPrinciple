## 单例模式
### 介绍
```
确保一个类只有一个实例，自行实例化并向系统提供这个实例。
```
### 设计
```
1. 懒汉式
2. 饿汉式
3. 双重校验锁式
4. 静态内部类式
5. 枚举式
```
### 应用场景
* 要求唯一生成序列号的环境
* 项目中一个Web页面的页面计数器
* 创建一个对象需要消耗的资源过多时，多对IO/数据库的访问
