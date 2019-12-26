package dev.lpf.utils;

/**
 * @program: JavaPrinciple
 * @description:
 * @author: Li Pengfei
 * @create: 2019-12-26 23:06
 **/
public class StringUtils {

    public static boolean isEmpty(String source) {
        if(source == null || source.equals("")) {
            return true;
        }
        return false;
    }
}
