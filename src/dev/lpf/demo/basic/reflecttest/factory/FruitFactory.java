package dev.lpf.demo.basic.reflecttest.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * FruitFactory
 *
 *
 */
public class FruitFactory {
    public static final String prefix = FruitFactory.class.getPackage().getName();

    /**
     * 获取水果实例
     *
     * @param fruitName 水果名
     * @return
     */
    public static Fruit getInstance(String fruitName) {
        Fruit fruit = null;
        try {
            fruit = (Fruit) Class.forName(prefix + "." + fruitName).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return fruit;
    }

    /**
     * 通过配置文件获取水果实例
     *
     * @return
     */
    public static List getInstanceFromProFile() throws IOException {
        Properties props = new Properties();
        File f = new File("fruit.properties");
        if (f.exists()) {
            props.load(new FileInputStream(f));
        } else {
            props.setProperty("apple", "Apple");
            props.setProperty("banana", "Banana");
            props.store(new FileOutputStream(f), "Store Fruit Class");
        }
        List list = new ArrayList();
        Set<Map.Entry<Object, Object>> entryset = props.entrySet();
        entryset.forEach(e->{
            list.add(getInstance((String) e.getValue()));
        });
        return list;
    }

}
