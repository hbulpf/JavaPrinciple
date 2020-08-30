package dev.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: RunAtWorld
 * @Date: 2020/4/22 23:52
 */
public class SummaryCase {
    public static void main(String[] args) throws InterruptedException {
        List<Object> caches = new ArrayList<Object>();
        for (int i = 0; i < 7; i++) {
            caches.add(new byte[1024 * 1024 * 3]);
            Thread.sleep(1000);
        }
        caches.clear();
        for (int i = 0; i < 2; i++) {
            caches.add(new byte[1024 * 1024 * 3]);
            Thread.sleep(1000);
        }
    }
}
