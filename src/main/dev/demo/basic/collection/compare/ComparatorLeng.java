
package dev.lpf.demo.basic.collection.compare;

import java.util.Comparator;

/**
 * Comparator Leng
 */
public class ComparatorLeng implements Comparator<String> {

    //标识两个对象是相等的属性
    String o1 = "sacWDVWdvWDSCDXCsfsdf";

    String o2 = "FWEFxsafcsdavxFWEFVcss";

    /**
     * 按照字符串中字符个数排序
     */
    @Override
    public int compare(String o1, String o2) {
        // return o1.compareTo(o2);
        return o1.length()-o2.length();
    }


    @Override
    public boolean equals(Object o) {
        Comparator com = (Comparator) o;
        return this.compare(o1, o2) == com.compare(o1, o2);
    }

    /**
     * 按照字符字典顺序升序排序
     * @param arr
     * @return
     */
    public Comparable[] sort(Comparable<String>[] arr) {
        // 将数组按照升序排列
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            // 将arr[i]插入到arr[i-1],arr[i-2]....之中去
            for (int j = i; j > 0 && less(arr[j], arr[j - 1]); j--) {
                // 交换arr[j]和arr[j-1]元素
                exch(arr, j, j - 1);
            }
        }
        return arr;
    }

    // 比较数组元素
    public boolean less(Comparable c1, Comparable c2) {
        return c1.compareTo(c2) < 0;
    }

    // 交换元素
    public void exch(Comparable[] com, int i, int j) {
        Comparable ce = com[i];
        com[i] = com[j];
        com[j] = ce;
    }
}
