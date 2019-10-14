package dev.lpf.basic.reflecttest.entity;

import java.util.Calendar;
import java.util.Date;

/**
 * 人
 */
public class Person implements China {
    public final static String RegType = "IDCard";
    private Integer id;
    private String name;
    private Integer age;

    public Person() {
    }

    private String hi() {
        System.out.println("hi~");
        return "hi~";
    }

    public Date getBorthDay() {
        Date now = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -age);
        return calendar.getTime();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Person(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Person(Integer id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }


    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public void sayChinese() {
        System.out.println("我是中国人");
    }

    @Override
    public void sayHello(String name, int age) {
        System.out.println("你好,我的名字是：" + name + ",我今年" + age + "岁啦！");
    }
}
