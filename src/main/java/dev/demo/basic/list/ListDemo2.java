
package dev.demo.basic.list;

import dev.demo.entity.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * List Demo2
 */
public class ListDemo2 {
    public static void main(String[] args) {
        test01();
        array2List();
        test03();
        arrayList2String();
        string2ArrayList();

        test05();
    }

    /**
     * List 转成 Array
     */
    public static void test01() {
        List<String> list = new ArrayList();
        list.add("aaa");
        list.add("bbb");
        // 方法1：得到 Object[] 数组，对其逐个转化
        Object[] objects = list.toArray();
        for (Object o : objects) {
            System.out.printf("%s\t", o.toString());
        }

        System.out.println();
        // 方法2:转换成数组
        String[] arrString = list.toArray(new String[list.size()]);
        System.out.println(arrString.toString());
    }

    /**
     * Array 转成 List
     */
    public static void array2List() {
        String[] array = new String[] {"aaa", "bbb"};
        System.out.println(Arrays.toString(array));
        // 转换方法1
        // List list = Arrays.stream(arr).boxed().collect(Collectors.toList());
        List list = Arrays.asList(array);
        System.out.println(list.toString());
        // list.add("hiss");
        // 上面语句会报异常，解决方法是新建一个List对象,
        List listNew = new ArrayList(list);
        listNew.add("hisxs");
        System.out.println(listNew.toString());
    }

    /**
     * List 转成 Array 效率验证
     * https://www.jianshu.com/p/7eee157f74fc
     */
    private static void test03() {
        ArrayList<String> list = new ArrayList();
        list.add("111");
        list.add("2222");

        // String[] 数组 size = strList.size() true
        String[] strArr1 = new String[list.size()];
        String[] strArr2 = list.toArray(strArr1);
        System.out.println(strArr1 == strArr2);
        System.out.println(strArr2.toString());

        // String[] 数组 size < strList.size() false
        String[] strs1 = new String[0];
        String[] strs2 = list.toArray(strArr1);
        System.out.println(strs1 == strs2);
        System.out.println(strs1.toString());

        // String[] 数组 size > strList.size() true
        String[] strs3 = new String[list.size() + 1];
        String[] strs4 = list.toArray(strs3);
        System.out.println(strs3 == strs4);
        System.out.println(strs3.toString());
    }

    /**
     * List 转换为 String
     */
    private static void arrayList2String() {
        List list = new ArrayList();
        list.add("aaa");
        list.add("bbb");
        // 转换成数组
        String str = String.join(",", list);
        System.out.println(str);
    }

    /**
     * String转换为 List
     */
    private static void string2ArrayList() {
        List list = new ArrayList();
        list.add("aaa");
        list.add("bbb");
        // 转换成数组
        String str = "1,2,3,4,5";
        ArrayList arrayList = new ArrayList(Collections.singleton(str.split(",")));
        System.out.println(arrayList.toString());
    }

    private static void test05() {
        List<Student> list = new ArrayList<Student>();
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(list);
    }

    private static void test06() {
        List<Student> list = new ArrayList<Student>();
        list.add(new Student("张红",23,223));
        ArrayList arrayList = new ArrayList();
        arrayList.add(list.get(0).getAge()) ;
    }
}
