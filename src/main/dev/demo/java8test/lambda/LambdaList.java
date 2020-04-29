
package dev.demo.java8.lambda;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Consumer;

import dev.demo.entity.Item;

/**
 * Lambda List
 */
interface ItemCreatorBlankConstruct {
    Item getItem();
}

interface ItemCreatorParamContruct {
    Item getItem(int id, String name, double price);
}

public class LambdaList {
    public static void main(String[] args) {
        test01();
        System.out.println("----");
        testSort();
        System.out.println("----");
        testClosure();
    }

    public static void test01() {
        ItemCreatorBlankConstruct creator = () -> new Item();
        Item item = creator.getItem();

        ItemCreatorBlankConstruct creator2 = Item::new;
        Item item2 = creator2.getItem();

        ItemCreatorParamContruct creator3 = Item::new;
        Item item3 = creator3.getItem(112, "鼠标", 135.99);
    }

    /**
     * 在以前我们若要为集合内的元素排序，就必须调用 sort 方法，
     * 传入比较器匿名内部类重写 compare 方法，我们现在可以使用 lambda 表达式来简化代码。
     */
    public static void testSort() {
        ArrayList<Item> list = new ArrayList<>();
        list.add(new Item(13, "背心", 7.80));
        list.add(new Item(11, "半袖", 37.80));
        list.add(new Item(14, "风衣", 139.80));
        list.add(new Item(12, "秋裤", 55.33));

        // 使用Comparator
        list.sort(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getId() - o2.getId();
            }
        });
        System.out.println(list);

        // 使用 Lambda sort
        list.add(new Item(17, "背心", 7.80));
        list.add(new Item(4, "半袖", 37.80));
        list.add(new Item(2, "风衣", 139.80));
        list.add(new Item(1, "秋裤", 55.33));

        System.out.println("sort by id...");
        list.sort((o1, o2) -> o1.getId() - o2.getId());
        list.forEach(System.out::println);
        System.out.println("sort by price...");
        list.sort((o1, o2) -> {
            return o1.getPrice() < o2.getPrice() ? -1 : 1;
        });
        list.forEach(System.out::println);

    }

    /**
     * 这个问题我们在匿名内部类中也会存在，如果我们把注释放开会报错，
     * 告诉我 num 值是 final 不能被改变。这里我们虽然没有标识 num 类型为 final，
     * 但是在编译期间虚拟机会帮我们加上 final 修饰关键字。
     */
    public static void testClosure() {

        int num = 10;

        Consumer<String> consumer = ele -> {
            System.out.println(num);
        };

        // num = num + 2;
        consumer.accept("hello");
    }
}