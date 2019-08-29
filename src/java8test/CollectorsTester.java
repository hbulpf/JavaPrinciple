package com.lpf.java8test;

import com.lpf.java8test.entity.Person;

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

        Map<Integer, Person> mapp = list.stream().collect(Collectors.toMap(Person::getId, Function.identity()));
        System.out.println(mapp);
        System.out.println(mapp.get(1).getName());
        Map<Integer, String> map = list.stream().collect(Collectors.toMap(Person::getId, Person::getName));
        System.out.println(map);

        //Collectors.toMap(keyMapper,valueMapper,mergeFunction)
        Map<String, Person> map2 = list.stream().collect(Collectors.toMap(Person::getName,b -> b, (k1, k2) -> k1));
        System.out.println(map2);


    }
}
