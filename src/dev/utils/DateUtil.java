package dev.utils;

import java.sql.Timestamp;

public class DateUtil {
    /**
     * 输出时间格式为：yyyy-MM-dd hh:mm:ss.
     *
     * @return
     */
    public static Timestamp getCurrentTime() {
        return new Timestamp(System.currentTimeMillis());
    }

}
