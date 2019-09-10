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


    /**
     * 获取一天的开始 UTC时间
     * @return
     */
    public static String  getBeginTimeOfDay() {
        Calendar cal = Calendar.getInstance();
        //服务器是UTC时间,一天开始时间东八区CST 24:00 - 8为服务器对应的时间，即UTC 16:00
        if (cal.get(Calendar.HOUR_OF_DAY) < 16) {
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime()) + "T16:00:00Z";
    }
}
