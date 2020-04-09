package dev.demo.basic.queue;

import dev.demo.entity.Student;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * PriorityQueue 会默认对元素进行升序排序
 *
 * @Author: RunAtWorld
 * @Date: 2020/4/2 23:10
 */
public class PriorityQueueTest {

    public static void main(String[] args) {
        basictest();
        orderStudentByScore();
    }

    /**
     * 自然排序
     */
    public static void basictest() {
        PriorityQueue<String> queue = new PriorityQueue<>();
        //入队
        queue.add("2");
        queue.add("6");
        queue.add("4");
        queue.add("3");
        queue.add("1");

        int len = queue.size();
        //出队列:升序排列
        for (int i = 0; i < len; i++) {
            System.out.printf("第%d次出队:%s\n", i, queue.poll());
        }
    }

    /**
     * 按照学生成绩
     * 自定义排序:先按照分数排序，再按照名字排序
     */
    public static void orderStudentByScore() {
        PriorityQueue<Student> queue = new PriorityQueue<>(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                if (o1.getScore() == o2.getScore()) {
                    return o1.getName().compareTo(o2.getName());
                }
                return o1.getScore() - o2.getScore();
            }
        });
        queue.add(new Student("zhang", 90));
        queue.add(new Student("chen", 91));
        queue.add(new Student("wang", 90));
        queue.add(new Student("zhao", 89));
        queue.add(new Student("qian", 78));
        queue.add(new Student("yuan", 100));

        int len = queue.size();
        Student stu = null;
        //出队列
        for (int i = 0; i < len; i++) {
            stu = queue.poll();
            System.out.printf("%d: %8s  %d\n", i, stu.getName(), stu.getScore());
        }

/*      结果:
        0:     qian  78
        1:     zhao  89
        2:     wang  90
        3:    zhang  90
        4:     chen  91
        5:     yuan  100
        */
    }

}
