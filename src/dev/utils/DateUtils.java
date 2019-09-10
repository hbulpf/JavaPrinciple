package test.lpf.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {
    /**
     *
     * 输出时间格式为：yyyy-MM-dd hh:mm:ss.
     *
     * @return
     */
    public static Timestamp getCurrentTime()
    {
        return new Timestamp(System.currentTimeMillis());
    }

}
