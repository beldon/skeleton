package skeleton.common.util;

import java.util.Date;

/**
 * @author Beldon
 */
public class RelativeDateFormat {
    private static final long ONE_MINUTE = 60000L;
    private static final long FORTY_FIVE_MINUTE = 45 * 60000L;
    private static final long ONE_HOUR = 60 * ONE_MINUTE;
    private static final long ONE_DAY = 24 * ONE_HOUR;
    private static final long TWO_DAY = 2 * ONE_DAY;
    private static final long ONE_MONTH = 30 * ONE_DAY;
    private static final long ONE_YEAR = 12 * ONE_MONTH;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";
    private static final String YESTERDAY = "昨天";

    private RelativeDateFormat() {

    }

    public static String format(Date date) {
        long delta = System.currentTimeMillis() - date.getTime();
        if (delta < ONE_MINUTE) {
            return populateMessage(toSeconds(delta), ONE_SECOND_AGO);

        }
        if (delta < FORTY_FIVE_MINUTE) {
            return populateMessage(toMinutes(delta), ONE_MINUTE_AGO);
        }
        if (delta < ONE_DAY) {
            return populateMessage(toHours(delta), ONE_HOUR_AGO);
        }
        if (delta < TWO_DAY) {
            return YESTERDAY;
        }
        if (delta < ONE_MONTH) {
            return populateMessage(toDays(delta), ONE_DAY_AGO);
        }
        if (delta < ONE_YEAR) {
            return populateMessage(toMonths(delta), ONE_MONTH_AGO);
        }
        return populateMessage(toYears(delta), ONE_YEAR_AGO);
    }

    private static String populateMessage(long time, String suffix) {
        return (time <= 0 ? 1 : time) + suffix;
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }
}
