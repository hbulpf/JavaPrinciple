package dev.lpf.demo.basic.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: JavaPrinciple
 * @description: List 使用测试
 *
 * https://blog.csdn.net/PitBXu/article/details/97672145
 * https://blog.csdn.net/qq_31564573/article/details/87906309
 * https://www.jianshu.com/p/84e4820993d3
 * @author: Li Pengfei
 * @create: 2019-12-28 18:23
 **/
public class MyList {
    public static void main(String[] args) {
        System.out.println("-------test01---------");
        test01();
        System.out.println("-------test02---------");
        test02();
        System.out.println("-------test03---------");
        test03();
    }

    public static void test01() {
        int[] arr = new int[]{1, 2, 3, 4, 5, 6, 7, 9, 92};
        System.out.println(Arrays.toString(arr));
        //int[] 转换为 List
        List list = Arrays.stream(arr).boxed().collect(Collectors.toList());
        int[] arr2 = new int[]{2, 43, 22, 7};
        for (int i : arr2) {
            if (list.contains(Integer.valueOf(i))) {
                System.out.printf("%d\t",i);
            }
        }

        list.add(23);

        System.out.println();
    }

    public static void test02() {
        List<String> roledList = new ArrayList<String>();
        roledList.add("1");
        roledList.add("2");
        roledList.add("3");

        Integer[] oneRoles = new Integer[]{1, 3};
        for (Integer i : oneRoles) {
            if (roledList.contains(i.toString())) {
                System.out.println(i);
            }
        }
    }

    public static void test03() {
        int[] arr2 = new int[]{2, 43, 22, 7};
        List list = new ArrayList<Integer>();
        list.add(3);
        list.add(7);
        for (int i : arr2) {
            if (list.contains(i)) {
                System.out.println(i);
            }
        }
    }
}
