
package dev.demo.entity;

import java.io.Serializable;

/**
 * 雇员信息
 */
public class Employee implements Serializable {

    private static final long serialVersionUID = 4775629632953317597L;

    /**
     * ID
     */
    public int id;

    /**
     * 级别
     */
    public int level;

    /**
     * 工资
     */
    public int salary;

    /**
     * 入职年数
     */
    public int year;

    public Employee(int id, int level, int salary, int year) {
        this.id = id;
        this.level = level;
        this.salary = salary;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
