package dev.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 输出时间格式为：yyyy-MM-dd hh:mm:ss.
     *
     * @return
     */
    public static Timestamp getCurrentTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 日期字符串转为 TimeStamp
     *
     * @param dateStr 日期字符串
     * @return 日期对应的凌晨 00:00:00.0
     */
    public static Timestamp dateStr2TimeStamp(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return new Timestamp(sdf.parse(dateStr.trim()).getTime());
        } catch (ParseException e) {
            System.out.println(dateStr + "日期格式错误，转换为时间戳失败！");
        }
        return null;
    }

    /**
     * 获取一天的开始 00:00:00.0
     *
     * @param dateStr 日期字符串
     * @return 日期对应的开始时间戳
     */
    public static Timestamp getBeginTimeOfDay(String dateStr) {
        if (null == dateStr) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return tranStringToTimeStamp(dateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取一天的结束 23:59:59.0
     * @param dateStr 日期字符串
     * @return 日期对应的结束时间戳
     */
    public static Timestamp getEndTimeOfDay(String dateStr) {
        if (null == dateStr) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        //String timestr = formatter.format(dateStr2TimeStamp(dateStr)) + " 23:59:59";
        return tranStringToTimeStamp( dateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 转换字符串到 java.sql.timestamp
     *
     * @param source
     * @param pattern
     * @return java.sql.Timestamp
     */
    public static Timestamp tranStringToTimeStamp(String source, String pattern) {
        try {
            if (StringUtils.isEmpty(pattern)) {
                pattern = "yyyy/MM/dd";
            }
            if (StringUtils.isEmpty(source)) {
                return null;
            }
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return new Timestamp(format.parse(source).getTime());
        } catch (ParseException e) {
            System.out.println("Parse time string [" + source + "] to timestamp failed.");
        }
        return null;
    }


    public static Timestamp toEndTimestamp(String formatTime)
    {
        // 短日期长度
        final int SHORT_DATE_LENGTH = 10;
        if (formatTime.length() <= SHORT_DATE_LENGTH)
        {
            // 补上时分秒
            formatTime += " 23:59:59";
        }
        return Timestamp.valueOf(formatTime);
    }


    /**
     * 判定彼此时间是否交叉
     * @param selfBegin  自身开始时间
     * @param selfEnd    自身结束时间
     * @param otherBegin  其他实体开始时间
     * @param otherEnd    其他实体结束时间
     * @return false:交叉; true:不交叉
     */
    public static boolean isNotTimeCrossed(Timestamp selfBegin, Timestamp selfEnd, Timestamp otherBegin,
                                           Timestamp otherEnd) {
        if (otherBegin.after(selfEnd) && otherEnd.after(selfEnd)) {
            return true;
        }
        if (otherBegin.before(selfBegin) && otherEnd.before(selfBegin)) {
            return true;
        }
        return false;
    }



    public static void main(String[] args) throws ParseException {
        System.out.println(dateStr2TimeStamp("2019-09-03"));
        System.out.println(getEndTimeOfDay("2019-09-03"));
        System.out.println(getBeginTimeOfDay("2019-09-03"));
        System.out.println(toEndTimestamp("2019-09-03"));

        // 交叉
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp selfBegin = new Timestamp(format.parse("2019-05-12 23:59:59").getTime());
        Timestamp selfEnd = new Timestamp(format.parse("2019-05-14 23:59:59").getTime());
        Timestamp otherBegin = new Timestamp(format.parse("2019-05-11 23:59:59").getTime());
        Timestamp otherEnd = new Timestamp(format.parse("2019-05-13 23:59:59").getTime());
        System.out.println(DateUtil.isNotTimeCrossed(selfBegin, selfEnd, otherBegin, otherEnd));

        // 交叉
        selfBegin = new Timestamp(format.parse("2019-05-12 23:59:59").getTime());
        selfEnd = new Timestamp(format.parse("2019-05-14 23:59:59").getTime());
        otherBegin = new Timestamp(format.parse("2019-05-13 23:59:59").getTime());
        otherEnd = new Timestamp(format.parse("2019-05-16 23:59:59").getTime());
        System.out.println(DateUtil.isNotTimeCrossed(selfBegin, selfEnd, otherBegin, otherEnd));

        // 交叉
        selfBegin = new Timestamp(format.parse("2019-05-12 23:59:59").getTime());
        selfEnd = new Timestamp(format.parse("2019-05-14 23:59:59").getTime());
        otherBegin = new Timestamp(format.parse("2019-05-11 23:59:59").getTime());
        otherEnd = new Timestamp(format.parse("2019-05-16 23:59:59").getTime());
        System.out.println(DateUtil.isNotTimeCrossed(selfBegin, selfEnd, otherBegin, otherEnd));

        // 交叉
        selfBegin = new Timestamp(format.parse("2019-05-11 23:59:59").getTime());
        selfEnd = new Timestamp(format.parse("2019-05-14 23:59:59").getTime());
        otherBegin = new Timestamp(format.parse("2019-05-12 23:59:59").getTime());
        otherEnd = new Timestamp(format.parse("2019-05-13 23:59:59").getTime());
        System.out.println(DateUtil.isNotTimeCrossed(selfBegin, selfEnd, otherBegin, otherEnd));

        // 不交叉
        selfBegin = new Timestamp(format.parse("2019-05-11 23:59:59").getTime());
        selfEnd = new Timestamp(format.parse("2019-05-14 23:59:59").getTime());
        otherBegin = new Timestamp(format.parse("2019-05-15 23:59:59").getTime());
        otherEnd = new Timestamp(format.parse("2019-05-17 23:59:59").getTime());
        System.out.println(DateUtil.isNotTimeCrossed(selfBegin, selfEnd, otherBegin, otherEnd));

        // 不交叉
        selfBegin = new Timestamp(format.parse("2019-05-11 23:59:59").getTime());
        selfEnd = new Timestamp(format.parse("2019-05-14 23:59:59").getTime());
        otherBegin = new Timestamp(format.parse("2019-05-03 23:59:59").getTime());
        otherEnd = new Timestamp(format.parse("2019-05-10 23:59:59").getTime());
        System.out.println(DateUtil.isNotTimeCrossed(selfBegin, selfEnd, otherBegin, otherEnd));

        // 不交叉
        selfBegin = new Timestamp(format.parse("2019-05-11 23:59:59").getTime());
        selfEnd = new Timestamp(format.parse("2019-05-14 23:59:59").getTime());
        otherBegin = new Timestamp(format.parse("2019-05-03 23:59:59").getTime());
        otherEnd = new Timestamp(format.parse("2019-05-11 23:59:59").getTime());
        System.out.println(DateUtil.isNotTimeCrossed(selfBegin, selfEnd, otherBegin, otherEnd));
    }


}
