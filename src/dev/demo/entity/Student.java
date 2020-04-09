
package dev.lpf.demo.entity;

/**
 * 学生
 */
public class Student {
    private int num;

    private String name;

    private int score;

    private int age;

    public Student() {
    }

    public Student(String name, int age, int num) {
        this.name = name;
        this.num = num;
        this.age = age;
    }

    public Student(int num, String name, int age, int score) {
        this.num = num;
        this.name = name;
        this.score = score;
        this.age = age;
    }

    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" + "num=" + num + ", name='" + name + '\'' + ", score=" + score + '}';
    }

}