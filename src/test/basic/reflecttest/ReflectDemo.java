package dev.lpf.basic.reflecttest;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import dev.lpf.basic.reflecttest.entity.Person;
import dev.lpf.basic.reflecttest.proxy.ISubject;
import dev.lpf.basic.reflecttest.proxy.MyInvocationHandler;
import dev.lpf.basic.reflecttest.proxy.RealSubject;

/**
 * 反射demo
 */
public class ReflectDemo {
    private String className = "dev.lpf.basic.reflecttest.Person";

    public static void main(String[] args) {
        ReflectDemo demo = new ReflectDemo();
        demo.testSimpleReflection();
        System.out.println();
        demo.getObjectByReflection();
        System.out.println();
        demo.getClassNameByInstance();
        System.out.println();
        demo.waysOfgettingClass();
        System.out.println();
        demo.getConstructonbyReflection();
        System.out.println();
        demo.getSuperbyReflection();
        System.out.println();
        demo.getMethodsbyReflection();
        System.out.println();
        demo.getFieldsByReflection();
        System.out.println();
        demo.arrayOperateByReflection();

        System.out.println();
        //使用动态Proxy 动态创建代理类
        MyInvocationHandler handler = new MyInvocationHandler();
        ISubject sub =(ISubject) handler.bind(new RealSubject());
        System.out.println(sub.say("北京中建"));
    }

    /**
     * 简单测试
     */
    private void testSimpleReflection() {
        Class<?> classType = Person.class;
        System.out.println(classType.getTypeName());
        System.out.println(classType.getName());
        System.out.println(classType.getAnnotations());
        System.out.println(classType.getClasses());
        System.out.println(classType.getClass());
        System.out.println(classType.getClass().getName());
        // 结果
        //dev.lpf.basic.reflecttest.Person
        //dev.lpf.basic.reflecttest.Person
        //[Ljava.lang.annotation.Annotation;@4554617c
        //[Ljava.lang.Class;@74a14482
        //class java.lang.Class
        //java.lang.Class
    }

    /**
     * 通过反射获取对象
     * 以后再编写使用 Class 实例化其他类的对象的时候，一定要自己定义无参的构造函数，否则会出现异常
     * java.lang.InstantiationException: dev.lpf.basic.reflecttest.Person
     */
    private void getObjectByReflection() {
        Class demo = null;
        try {
            demo = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Person person = null;
        try {
            person = (Person) demo.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        person.setName("王燕");
        person.setId(8723);
        person.setAge(26);
        System.out.println(person);
    }

    /**
     * 通过一个对象获得完整的包名 和类名
     * 注意：所有类的对象其实都是 Class 的实例。
     */
    private void getClassNameByInstance() {
        Person p = new Person(1223, "zhang");
        System.out.println(p.getClass().getName());
    }

    /**
     * 获取类类型对象的多种方式
     */
    private void waysOfgettingClass() {
        Class p1 = null, p2, p3;
        try {
            //建议采用这种方式
            p1 = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        p2 = new Person(1232, "Han").getClass();
        p3 = Person.class;

        System.out.println("类名称: " + p1.getName());
        System.out.println("类名称: " + p2.getName());
        System.out.println("类名称: " + p3.getName());
    }


    /**
     * 通过反射获取对象的 构造方法,并通过构造方法产生实例
     */
    private void getConstructonbyReflection() {
        Class aClass = null;
        try {
            aClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //获取构造方法
        Constructor[] cons = aClass.getConstructors();
        Arrays.stream(cons).forEach(c -> {
            int mo = c.getModifiers();
            System.out.println("构造方法: " + Modifier.toString(mo) + " " + c.getName());
            Class params[] = c.getParameterTypes();
            Arrays.stream(params).forEach(p -> {
                System.out.println(" - 参数: " + p.getName() + "  ");
            });
            System.out.println(" - 异常: " + c.getExceptionTypes());
        });

        System.out.println();
        cons = aClass.getDeclaredConstructors();
        Arrays.stream(cons).forEach(c -> {
            System.out.println("构造方法名: " + c.getName());
        });

        //使用反射创建实例
        Person[] people = new Person[cons.length];
        try {
            people[0] = (Person) cons[0].newInstance(12, "成");
            people[1] = (Person) cons[1].newInstance(13, "HKE", 35);
            people[2] = (Person) cons[2].newInstance();
            for (Person person : people) {
                System.out.println(person.toString());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过反射获取对象的 实现的接口、父类
     */
    private void getSuperbyReflection() {
        Class aClass = null;
        try {
            aClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Class supper = aClass.getSuperclass();
        System.out.println("继承的父类:" + supper.getName());
        System.out.println("实现的接口:");
        Class[] interfaces = aClass.getInterfaces();
        Arrays.stream(interfaces).forEach(i -> {
            System.out.println(i.getName());
        });
    }


    /**
     * 通过反射获取对象的 方法,调用某个方法
     */
    private void getMethodsbyReflection() {
        Class demo = null;
        try {
            demo = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //获取所有方法
        Method[] methods = demo.getDeclaredMethods();
        Arrays.stream(methods).forEach(m -> {
            int mo = m.getModifiers();
            System.out.println("方法: " + Modifier.toString(mo) + " " + m.getName());
            Class params[] = m.getParameterTypes();
            System.out.print(" - 参数: ");
            Arrays.stream(params).forEach(p -> {
                System.out.print(p.getName() + ",  ");
            });
            System.out.println();
            System.out.println(" - 异常: " + m.getExceptionTypes());
            System.out.println();
        });

        //获取某个方法,调用某个方法
        try {
            Method method1 = demo.getMethod("getBorthDay");
            System.out.println("getBorthDay方法:" + method1.getName());
            //调用getBorthDay方法
            Person person = (Person) demo.newInstance();
            person.setAge(18);
            person.setName("隆隆");
            System.out.println(person.getName() + "  生于 " + method1.invoke(person));
            //调用sayHello
            Method method2 = demo.getMethod("sayHello", String.class, int.class);
            method2.invoke(demo.newInstance(), "陈升", 40);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用某个方法
    }

    /**
     * 通过反射获取对象的 属性、实现接口和父类的属性，设置属性
     */
    private void getFieldsByReflection() {
        Class demo = null;
        try {
            demo = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //获取所有本类属性
        System.out.println("--本类属性--");
        Field[] fields = demo.getDeclaredFields();
        Arrays.stream(fields).forEach(f -> {
            int mo = f.getModifiers();
            Class fType = f.getType();
            System.out.println("属性: " + Modifier.toString(mo) + " " + fType.getName() + " " + f.getName());
        });

        //获取所有实现接口和父类属性
        System.out.println("--实现接口和父类属性--");
        fields = demo.getFields();
        Arrays.stream(fields).forEach(f -> {
            int mo = f.getModifiers();
            Class fType = f.getType();
            System.out.println(" - 接口或父类属性: " + Modifier.toString(mo) + " " + fType.getName() + " " + f.getName());
        });


        //获取某个属性,设置某个属性
        try {
            //获取本类某个属性
            Field field1 = demo.getDeclaredField("age");
            //获取接口某个属性
            Field field2 = demo.getField("country");

            //设置某个属性
            Person person = (Person) demo.newInstance();
            person.setAge(19);
            person.setName("明令隆");
            // 私有属性要设置可访问，否则报异常 can not access a member of class dev.lpf.basic.reflecttest.Person with modifiers "private"
            field1.setAccessible(true);
            System.out.println("使用反射设置前：age=" + field1.get(person));
            field1.set(person, 39);
            System.out.println("使用反射设置后: age=" + field1.get(person));

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param obj  操作的对象
     * @param attr 操作的属性
     */
    public static void getter(Object obj, String attr) {
        Field field = null;
        try {
            field = obj.getClass().getField(attr);
            field.setAccessible(true);
            field.get(obj);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param obj   操作的对象
     * @param attr  操作的属性
     * @param value 设置属性值
     * @param type  参数的属性
     */
    public static void setter(Object obj, String attr, Object value, Class<?> type) {
        Field field = null;
        try {
            field = obj.getClass().getField(attr);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数组操作
     */
    public void arrayOperateByReflection() {
        int[] arr = {1, 2, 3, 4, 5};
        Class<?> demo = arr.getClass().getComponentType();
        //获取数组基本信息
        System.out.println("数组类型:" + demo.getName());
        System.out.println("数组长度:" + Array.getLength(arr));
        System.out.println("数组第1个元素:" + Array.get(arr, 0));
        Array.set(arr, 0, 100);
        System.out.println("数组第1个元素修改为:" + Array.get(arr, 0));
        printArr(arr);
        //修改数组大小
        Object newArr = Array.newInstance(demo, 20);
        printArr(newArr);
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        arr = (int[]) newArr;
        printArr(arr);
    }

    /**
     * 打印数组对象
     * @param obj 数组对象实例
     */
    public static void printArr(Object obj){
        Class aClass = obj.getClass();
        if(!aClass.isArray()){
            return;
        }
        System.out.print("数组长度:"+Array.getLength(obj)+"  数组:");
        for (int i = 0; i < Array.getLength(obj); i++) {
            System.out.print(Array.get(obj,i)+" ");
        }
        System.out.println();
    }
}
