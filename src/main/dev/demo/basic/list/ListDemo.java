package dev.lpf.demo.basic.collection.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * List Demo
 *
 */
class ListDemo {
    public static void main(String[] args) {
        System.out.println("-------Array2List---------");
        array2List();
        System.out.println("-------List包装String---------");
        listAndString();
        System.out.println("-------List包装Integer---------");
        listAndInteger();
    }

    /**
     * Array2List
     */
    public static void array2List() {
        int[] arr = new int[]{1, 2, 3, 4, 5, 6, 7, 9, 92};
        System.out.println(Arrays.toString(arr));
        //int[] 转换为 List
        List list = Arrays.stream(arr).boxed().collect(Collectors.toList());

        //打印
        int[] arr2 = new int[]{2, 43, 22, 7};
        for (int i : arr2) {
            if (list.contains(Integer.valueOf(i))) {
                System.out.printf("%d\t",i);
            }
        }
        list.add(23);
    }

    /**
     * List包装String
     */
    public static void listAndString() {
        List<String> roledList = new ArrayList<String>();
        roledList.add("1");
        roledList.add("2");
        roledList.add("3");

        //打印
        Integer[] oneRoles = new Integer[]{1, 3};
        for (Integer i : oneRoles) {
            if (roledList.contains(i.toString())) {
                System.out.println(i);
            }
        }
    }

    /**
     * List包装Integer
     */
    public static void listAndInteger() {
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
