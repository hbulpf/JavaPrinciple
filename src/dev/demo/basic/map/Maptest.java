package dev.demo.basic.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Maptest {
    public static void main(String[] args) {
        Map map = new HashMap<String, String>();
        map.put("zhang", "23");
        map.put("Li", "21");
        map.put("Chen", "12");
        String mapStr = map.toString();
        System.out.println(mapStr);
        Map map2 = mapStringToMap(mapStr);
        System.out.println(map2);
        mapStr = "{\"chang\"=\"23\", \"Xi\"=\"21\", \"Cen\"=\"12\"}";
        map2 = mapStringToMap(mapStr);
        System.out.println(map2.get("chang"));
        System.out.println(map2);

        List list = new ArrayList<String>();
        list.add("CN");
        list.add("US");
        list.add("JA");
        System.out.println(list);
    }

    public static Map<String, String> mapStringToMap(String str) {
        str = str.substring(1, str.length() - 1);
        str = str.replace("\"", "");
        String[] strs = str.split(",");
        Map<String, String> map = new HashMap<String, String>();
        for (String string : strs) {
            String key = string.split("=")[0];
            String value = string.split("=")[1];
            map.put(key, value);
        }
        return map;
    }


    /**
     * 遍历输出 Key value
     */
    public static void printKeyValue() {
        // 方式1
        Map<String, String> map = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            entry.getKey();
            entry.getValue();
        }
        // 方式2
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            entry.getKey();
            entry.getValue();
        }
        // 方式3 : 性能最差
        Map<String, String> map2 = new HashMap<String, String>();
        for (String key : map.keySet()) {
            map2.get(key);
        }
        // 方式4
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            entry.getKey();
            entry.getValue();
        }
    }
}
