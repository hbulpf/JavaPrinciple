package test.java8test;

import test.java8test.entity.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Java8  Collectors 测试
 */
public class CollectorsTester {
    public static void main(String[] args) {
        List<Person> list = new ArrayList();
        list.add(new Person(1, "haha"));
        list.add(new Person(2, "rere"));
        list.add(new Person(3, "fefe"));
        list.add(new Person(4, "fefe"));
        list.add(new Person(5, "fefe"));

        //1.1 将List转成 <id,Person> 的map
        Map<Integer, Person> map1 = list.stream().collect(Collectors.toMap(Person::getId, Function.identity()));
        System.out.println(map1);
        System.out.println(map1.get(1).getName());

        //1.2 将List转成 <id,Name> 的map
        Map<Integer, String> map2 = list.stream().collect(Collectors.toMap(Person::getId, Person::getName));
        System.out.println(map2);

        //1.3 将List转成 <Name,Person> 的map
        Map<String, Person> map3 = list.stream().collect(Collectors.toMap(Person::getName, b -> b, (k1, k2) -> k1));
        System.out.println(map3);

        //1.4 将List转成 <Name,Person> 的map:这里和1.3不一样，取Name相同的最后一个Person
        Map<String, Person> map4 = list.stream().collect(Collectors.toMap(Person::getName, b -> b, (k1, k2) -> k2));
        System.out.println(map4);

        System.out.println("++++++++++++++++++");

        //2.1 提取出list对象中的一个属性
        List<Integer> pIds = list.stream()
                .map(Person::getId)
                .collect(Collectors.toList());
        pIds.forEach(s -> System.out.print(s + " "));
        System.out.println();
        System.out.println("----------");

        //2.2 提取出list对象中的一个属性并去重
        List<String> pNames = list.stream().map(Person::getName).distinct().collect(Collectors.toList());
        pNames.forEach(s -> System.out.print(s + " "));
    }
}
