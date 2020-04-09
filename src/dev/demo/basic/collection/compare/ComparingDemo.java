
package dev.lpf.demo.basic.collection.compare;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import dev.lpf.demo.entity.Employee;
import dev.lpf.demo.entity.Student;

/**
 * Compare 使用
 */
public class ComparingDemo {

    public static void main(String[] args) {
        test01();
        test02();
        test03();
        test04CumsterOrder();
    }

    /**
     * 使用 Comparator 自定义排序: 级别,工资,工龄
     */
    public static void test01() {
        List<Employee> employeeList = new ArrayList<Employee>() {
            {
                add(new Employee(1, 9, 10000, 10));
                add(new Employee(2, 9, 12000, 7));
                add(new Employee(3, 5, 10000, 12));
                add(new Employee(4, 5, 10000, 6));
                add(new Employee(5, 3, 5000, 3));
                add(new Employee(6, 1, 2500, 1));
                add(new Employee(7, 5, 8000, 10));
                add(new Employee(8, 3, 8000, 2));
                add(new Employee(9, 1, 3000, 5));
                add(new Employee(10, 1, 2500, 4));
                add(new Employee(11, 2, 2000, 4));
            }
        };
        Collections.sort(employeeList, new EmpComparator());
        System.out.println("ID\tLevel\tSalary\tYears");
        System.out.println("=============================");
        for (Employee employee : employeeList) {
            System.out.printf("%d\t%d\t%d\t%d\n", employee.getId(), employee.getLevel(), employee.getSalary(),
                employee.getYear());
        }
        System.out.println("=============================");
    }

    /**
     * 使用 Lambda 表达式自定义排序: 工资,级别
     */
    public static void test02() {
        List<Employee> employeeList = new ArrayList<Employee>() {
            {
                add(new Employee(1, 9, 10000, 10));
                add(new Employee(2, 9, 12000, 7));
                add(new Employee(3, 5, 10000, 12));
                add(new Employee(4, 5, 10000, 6));
                add(new Employee(5, 3, 5000, 3));
                add(new Employee(6, 1, 2500, 1));
                add(new Employee(7, 5, 8000, 10));
                add(new Employee(8, 3, 8000, 2));
                add(new Employee(9, 1, 3000, 5));
                add(new Employee(10, 1, 2500, 4));
                add(new Employee(11, 2, 2000, 4));
            }
        };
        // 使用 Lambda 表达式自定义排序
        Collections.sort(employeeList,
            ((o1, o2) -> (o1.salary - o2.salary == 0 ? o1.level - o2.level : o1.salary - o2.salary)));

        System.out.println("ID\tLevel\tSalary\tYears");
        System.out.println("=============================");
        for (Employee employee : employeeList) {
            System.out.printf("%d\t%d\t%d\t%d\n", employee.getId(), employee.getLevel(), employee.getSalary(),
                employee.getYear());
        }
        System.out.println("=============================");
    }

    /**
     * 按照中文首字母排序
     */
    public static void test03() {
        List<Student> stus = new ArrayList<Student>() {
            {
                add(new Student("张三", 30, 1));
                add(new Student("李四", 20, 2));
                add(new Student("王五", 40, 3));
                add(new Student("赵六", 30, 4));
                add(new Student("陈七", 40, 5));
                add(new Student("周八", 20, 6));
            }
        };

        // 对学生集合按姓名首字母排序
        Comparator comparator = Collator.getInstance(Locale.CHINA);
        Collections.sort(stus, new Comparator<Student>() {

            @Override
            public int compare(Student s1, Student s2) {
                return comparator.compare(s1.getName(), s2.getName());
            }

        });
        System.out.println("年龄       学号       姓名  ");
        for (Student s : stus) {
            System.out.println(s.getAge() + "   " + s.getNum() + "   " + s.getName());
        }
    }

    /**
     * 自定义条件排序
     */
    public static void test04CumsterOrder() {
        String[] order = {"语文", "数学", "英语", "物理", "化学", "生物", "政治", "历史", "地理", "总分"};
        final List<String> definedOrder = Arrays.asList(order);
        List<String> list = new ArrayList<String>() {
            {
                add("总分");
                add("英语");
                add("政治");
                add("总分");
                add("数学");
            }
        };

        Collections.sort(list, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                int io1 = definedOrder.indexOf(o1);
                int io2 = definedOrder.indexOf(o2);
                return io1 - io2;
            }
        });
        // 或者
        // Collections.sort(list, (o1, o2)->(definedOrder .indexOf(o1)-definedOrder .indexOf(o2)));

        for (String s : list) {
            System.out.print(s + "   ");
        }
    }

}
