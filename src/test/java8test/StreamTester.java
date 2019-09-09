package test.java8test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Java8 Steram 测试
 */
public class StreamTester {
    public static void main(String[] args) {
        java8Test();
        java7Test();
    }

    public static void java8Test() {
        //Java8 Stream 测试
        //filter 测试
        System.out.println("---filter 测试---");
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        filtered.forEach(s -> System.out.println(s));
        System.out.printf("字符串个数:%d\n", filtered.stream().count());

        //forEach 测试
        System.out.println("---forEach 测试---");
        Random random = new Random();
        random.ints().limit(10).forEach(System.out::println);
        random.ints().limit(10).sorted().forEach(System.out::println);
        Stream.of(1, 2, 4, 5, 0, -1, 9).forEachOrdered(System.out::println);

        //sorted 测试
        System.out.println("---sorted 测试---");
        String[] arr = new String[]{"b_123", "c+342", "b#632", "d_123"};
        List<String> l = Arrays.stream(arr)
                .sorted((s1, s2) -> {
                    if (s1.charAt(0) == s2.charAt(0))
                        return s1.substring(2).compareTo(s2.substring(2));
                    else
                        return s1.charAt(0) - s2.charAt(0);
                })
                .collect(Collectors.toList());
        System.out.println(l); //[b_123, b#632, c+342, d_123]

        //map 测试
        System.out.println("---map 测试---");
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        //获取对应的平方数
        List<Integer> squaresList = numbers.stream().map(i -> i * i).distinct().collect(Collectors.toList());
        squaresList.stream().forEach(System.out::println);

        //reduce测试
        System.out.println("reduce测试");
        Optional<Integer> total = Stream.of(1, 2, 3, 4, 5).reduce((x, y) -> x + y);
        System.out.println("total= " + total);
        Integer total2 = Stream.of(1, 2, 3, 4, 5).reduce(0, (x, y) -> x + y);
        System.out.println("total2= " + total2);

        //并行（parallel）程序
        System.out.println("---并行（parallel）程序---");
        List<String> strs = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        //获取空字符串的数量
        long cnt = strs.parallelStream().filter(string -> string.isEmpty()).count();
        System.out.println(cnt);

        // Collectors:转换成集合和聚合元素
        System.out.println("---Collectors:转换成集合和聚合元素---");
        strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        System.out.println("筛选列表: " + filtered);
        String mergedString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
        System.out.println("合并字符串: " + mergedString);

        //peek 会使用一个Consumer消费流中的元素，但是返回的流还是包含原来的流中的元素
        System.out.println("---peek测试---");
        String[] charArr = new String[]{"a", "b", "c", "d"};
        Arrays.stream(charArr)
                .peek(System.out::println) //a,b,c,d
                .count();

        //flatmap 混合了map + flattern的功能，它将映射后的流的元素全部放入到一个新的流中
        System.out.println("---flatmap测试---");
        String poetry = "Where, before me, are the ages that have gone?\n" +
                "And where, behind me, are the coming generations?\n" +
                "I think of heaven and earth, without limit, without end,\n" +
                "And I am all alone and my tears fall down.";
        Stream<String> lines = Arrays.stream(poetry.split("\n"));
        Stream<String> words = lines.flatMap(line -> Arrays.stream(line.split(" ")));
        List<String> l2 = words.map(w -> {
            if (w.endsWith(",") || w.endsWith(".") || w.endsWith("?"))
                return w.substring(0, w.length() - 1).trim().toLowerCase();
            else
                return w.trim().toLowerCase();
        }).distinct().sorted().collect(Collectors.toList());
        System.out.println(l2);

        //统计测试
        System.out.println("---统计测试---");
        List<Integer> nums = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        IntSummaryStatistics stats = nums.stream().mapToInt((x) -> x).summaryStatistics();
        System.out.println("列表中最大的数 : " + stats.getMax());
        System.out.println("列表中最小的数 : " + stats.getMin());
        System.out.println("所有数之和 : " + stats.getSum());
        System.out.println("平均数 : " + stats.getAverage());

        //Match测试
        System.out.println("---Match测试---");
        System.out.println(Stream.of(1, 2, 3, 4, 5).allMatch(i -> i > 0)); //true
        System.out.println(Stream.of(1, 2, 3, 4, 5).anyMatch(i -> i > 0)); //true
        System.out.println(Stream.of(1, 2, 3, 4, 5).noneMatch(i -> i > 0)); //false
        System.out.println(Stream.<Integer>empty().allMatch(i -> i > 0)); //true
        System.out.println(Stream.<Integer>empty().anyMatch(i -> i > 0)); //false
        System.out.println(Stream.<Integer>empty().noneMatch(i -> i > 0)); //true
    }

    public static void java7Test() {
        System.out.println("使用 Java 7: ");

        // 计算空字符串
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        System.out.println("列表: " + strings);
        long count = getCountEmptyStringUsingJava7(strings);

        System.out.println("空字符数量为: " + count);
        count = getCountLength3UsingJava7(strings);

        System.out.println("字符串长度为 3 的数量为: " + count);

        // 删除空字符串
        List<String> filtered = deleteEmptyStringsUsingJava7(strings);
        System.out.println("筛选后的列表: " + filtered);

        // 删除空字符串，并使用逗号把它们合并起来
        String mergedString = getMergedStringUsingJava7(strings, ", ");
        System.out.println("合并字符串: " + mergedString);
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);

        // 获取列表元素平方数
        List<Integer> squaresList = getSquares(numbers);
        System.out.println("平方数列表: " + squaresList);
        List<Integer> integers = Arrays.asList(1, 2, 13, 4, 15, 6, 17, 8, 19);

        System.out.println("列表: " + integers);
        System.out.println("列表中最大的数 : " + getMax(integers));
        System.out.println("列表中最小的数 : " + getMin(integers));
        System.out.println("所有数之和 : " + getSum(integers));
        System.out.println("平均数 : " + getAverage(integers));
        System.out.println("随机数: ");

        // 输出10个随机数
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            System.out.println(random.nextInt());
        }
    }

    private static int getCountEmptyStringUsingJava7(List<String> strings) {
        int count = 0;

        for (String string : strings) {

            if (string.isEmpty()) {
                count++;
            }
        }
        return count;
    }

    private static int getCountLength3UsingJava7(List<String> strings) {
        int count = 0;

        for (String string : strings) {

            if (string.length() == 3) {
                count++;
            }
        }
        return count;
    }

    private static List<String> deleteEmptyStringsUsingJava7(List<String> strings) {
        List<String> filteredList = new ArrayList<String>();

        for (String string : strings) {

            if (!string.isEmpty()) {
                filteredList.add(string);
            }
        }
        return filteredList;
    }

    private static String getMergedStringUsingJava7(List<String> strings, String separator) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String string : strings) {

            if (!string.isEmpty()) {
                stringBuilder.append(string);
                stringBuilder.append(separator);
            }
        }
        String mergedString = stringBuilder.toString();
        return mergedString.substring(0, mergedString.length() - 2);
    }

    private static List<Integer> getSquares(List<Integer> numbers) {
        List<Integer> squaresList = new ArrayList<Integer>();

        for (Integer number : numbers) {
            Integer square = new Integer(number.intValue() * number.intValue());

            if (!squaresList.contains(square)) {
                squaresList.add(square);
            }
        }
        return squaresList;
    }

    private static int getMax(List<Integer> numbers) {
        int max = numbers.get(0);

        for (int i = 1; i < numbers.size(); i++) {

            Integer number = numbers.get(i);

            if (number.intValue() > max) {
                max = number.intValue();
            }
        }
        return max;
    }

    private static int getMin(List<Integer> numbers) {
        int min = numbers.get(0);

        for (int i = 1; i < numbers.size(); i++) {
            Integer number = numbers.get(i);

            if (number.intValue() < min) {
                min = number.intValue();
            }
        }
        return min;
    }

    private static int getSum(List numbers) {
        int sum = (int) (numbers.get(0));

        for (int i = 1; i < numbers.size(); i++) {
            sum += (int) numbers.get(i);
        }
        return sum;
    }

    private static int getAverage(List<Integer> numbers) {
        return getSum(numbers) / numbers.size();
    }
}
