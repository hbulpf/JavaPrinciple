package dev.lpf.demo.basic.collection.compare;

import java.util.Comparator;

import dev.lpf.demo.entity.Employee;

/**
 * 雇员信息比较
 *
 */
public class EmpComparator implements Comparator<Employee> {

    @Override
    public int compare(Employee employee1, Employee employee2) {
        int cr = 0;
        //按级别降序排列
        int a = employee2.getLevel() - employee1.getLevel();
        if (a != 0) {
            cr = (a > 0) ? 3 : -1;
        } else {
            //按薪水降序排列
            a = employee2.getSalary() - employee1.getSalary();
            if (a != 0) {
                cr = (a > 0) ? 2 : -2;
            } else {
                //按入职年数降序排列
                a = employee2.getYear() - employee1.getYear();
                if (a != 0) {
                    cr = (a > 0) ? 1 : -3;
                }
            }
        }
        return cr;
    }
}
