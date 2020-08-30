package dev.demo.basic.reflecttest.factory;

import java.io.IOException;

/**
 * 通过对象工厂获取对象
 *
 *
 */
public class testMain {
    public static void main(String[] args) {
        Fruit fruit = FruitFactory.getInstance("Banana");
        fruit.eat();

        try {
            FruitFactory.getInstanceFromProFile().stream().forEach(f->{
                ((Fruit)f).eat();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
