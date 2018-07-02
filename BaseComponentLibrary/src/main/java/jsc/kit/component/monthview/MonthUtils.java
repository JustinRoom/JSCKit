package jsc.kit.component.monthview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/JSCKit" target="_blank">https://github.com/JustinRoom/JSCKit</a>
 *
 * @author jiangshicheng
 */
public class MonthUtils {

    public static List<DayItem> getMonthDays(int year, int month) {
        List<DayItem> dayItems = new ArrayList<>();
        int maxShowCount = 5 * 7;
        addCurrentMonthDays(dayItems, year, month);
        addPreMonthDays(dayItems);
        addNextMonthDays(dayItems, maxShowCount - dayItems.size());
        return dayItems;
    }

    /**
     * 添加本月所有天数
     *
     * @param dayItems
     */
    private static void addCurrentMonthDays(List<DayItem> dayItems, int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        int curMonthDayCount = getMonthDayCount(calendar);

        for (int i = 0; i < curMonthDayCount; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i + 1);
            DayItem item = new DayItem();
            item.setKey(0);
            item.setDate(calendar.getTimeInMillis());
            item.setLabel(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            dayItems.add(item);
        }
    }

    /**
     * 添加上个月的最后几天
     *
     * @param dayItems
     */
    private static void addPreMonthDays(List<DayItem> dayItems) {
        DayItem firstItem = dayItems.get(0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(firstItem.getDate());
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        if (weekDay > Calendar.SUNDAY) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
            DayItem item = new DayItem();
            item.setKey(-1);
            item.setDate(calendar.getTimeInMillis());
            item.setLabel(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            dayItems.add(0, item);
            addPreMonthDays(dayItems);
        }
    }

    /**
     * 添加上个月的最后几天
     *
     * @param dayItems
     */
    private static void addNextMonthDays(List<DayItem> dayItems, int count) {
        if (count == 0)
            return;

        DayItem lastItem = dayItems.get(dayItems.size() - 1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastItem.getDate());
        for (int i = 0; i < count; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
            DayItem item = new DayItem();
            item.setKey(1);
            item.setDate(calendar.getTimeInMillis());
            item.setLabel(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            dayItems.add(item);
        }
    }

    /**
     * 获取某一个月的天数
     *
     * @param millis time stamp
     * @return 当月的天数
     */
    public static int getMonthDayCount(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return getMonthDayCount(calendar);
    }

    /**
     * 获取某一个月的天数
     *
     * @param date date
     * @return 当月的天数
     */
    public static int getMonthDayCount(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getMonthDayCount(calendar);
    }

    /**
     * 获取某一个月的天数
     *
     * @param calendar 日历
     * @return 当月天数
     */
    public static int getMonthDayCount(Calendar calendar) {
        if (calendar == null)
            calendar = Calendar.getInstance();
        return calendar.getActualMaximum(Calendar.DATE);
    }
}
