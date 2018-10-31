package jsc.kit.datetimepicker.widget;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import jsc.kit.datetimepicker.R;

/**
 * <p>
 * 日期时间选择器
 * <br>本库是基于<a href="https://github.com/liuwan1992/CustomDatePicker" target="_blank">https://github.com/liuwan1992/CustomDatePicker</a>上的修改提炼而成。
 * <br>非常感谢作者<a href="https://github.com/liuwan1992" target="_blank">liuwan1992</a>
 * </p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br>https://github.com/JustinRoom/JSCKit
 *
 * @author jiangshicheng
 */
public class DateTimePicker {

    /**
     * 定义结果回调接口
     */
    public interface ResultHandler {
        void handle(Date date);
    }

    public enum ShowType {
        YEAR(0),//显示年
        MONTH(1),//显示年、月
        DAY(2),//显示年、月、日
        HOUR(3),//显示年、月、日、时
        MINUTE(4);//显示年、月、日、时、分

        ShowType(int value) {
            this.value = value;
        }

        public int value;
    }

    private ResultHandler handler;
    private Context context;

    private Dialog datePickerDialog;
    private FrameLayout fyHeader;
    private LinearLayout lyBody;
    private View segmentLineView;
    private DatePickerView yearPicker, monthPicker, dayPicker, hourPicker, minutePicker;
    private TextView btnCancel, tvTitle, btnOK, yearLabel, monthLabel, dayLabel, hourLabel, minuteLabel;

    private static final int MAX_MINUTE = 59;
    private static final int MAX_HOUR = 23;
    private static final int MIN_MINUTE = 0;
    private static final int MIN_HOUR = 0;
    private static final int MAX_MONTH = 12;

    private ArrayList<String> years, months, days, hours, minutes;
    private int startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute;
    private Calendar selectedCalender, startCalendar, endCalendar;
    private ShowType curShowType;//显示级别
    private boolean keepLastSelected;//是否保留上一次的选择

    public DateTimePicker(@NonNull Context context, ResultHandler resultHandler, @NonNull Date startDate, @NonNull Date endDate) {
        this(context, resultHandler, startDate, endDate, null);
    }

    public DateTimePicker(@NonNull Context context, ResultHandler resultHandler, @NonNull Date startDate, @NonNull Date endDate, Builder builder) {
        if (startDate.after(endDate))
            throw new IllegalArgumentException("开始日期必须在结束日期之前。");
        this.context = context;
        this.handler = resultHandler;
        selectedCalender = Calendar.getInstance();
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        endCalendar.setTime(endDate);
        initDialog();
        initView();
        updateView(builder);
    }

    private void initDialog() {
        if (datePickerDialog == null) {
            datePickerDialog = new Dialog(context, R.style.DateTimePickerDialog);
            datePickerDialog.setCancelable(true);
            datePickerDialog.setCanceledOnTouchOutside(false);
            datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            datePickerDialog.setContentView(R.layout.date_time_picker_layout);
            Window window = datePickerDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = context.getResources().getDisplayMetrics().widthPixels;
            window.setAttributes(lp);
        }
    }

    private void initView() {
        fyHeader = datePickerDialog.findViewById(R.id.fy_header);
        lyBody = datePickerDialog.findViewById(R.id.ly_body);
        segmentLineView = datePickerDialog.findViewById(R.id.segment_line_view);
        yearPicker = datePickerDialog.findViewById(R.id.year_picker);
        monthPicker = datePickerDialog.findViewById(R.id.month_picker);
        dayPicker = datePickerDialog.findViewById(R.id.day_picker);
        hourPicker = datePickerDialog.findViewById(R.id.hour_picker);
        minutePicker = datePickerDialog.findViewById(R.id.minute_picker);
        btnCancel = datePickerDialog.findViewById(R.id.btn_cancel);
        tvTitle = datePickerDialog.findViewById(R.id.tv_title);
        btnOK = datePickerDialog.findViewById(R.id.btn_ok);
        yearLabel = datePickerDialog.findViewById(R.id.year_label);
        monthLabel = datePickerDialog.findViewById(R.id.month_label);
        dayLabel = datePickerDialog.findViewById(R.id.day_label);
        hourLabel = datePickerDialog.findViewById(R.id.hour_label);
        minuteLabel = datePickerDialog.findViewById(R.id.minute_label);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.handle(selectedCalender.getTime());
                datePickerDialog.dismiss();
            }
        });
    }

    private void updateView(Builder builder) {
        if (builder == null)
            builder = new Builder(context);

        fyHeader.setBackgroundColor(builder.headerBackgroundColor);
        lyBody.setBackgroundColor(builder.bodyBackgroundColor);
        segmentLineView.setBackgroundColor(builder.segmentingLineColor);
        segmentLineView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, builder.segmentingLineHeight));
        //
        yearPicker.setColors(builder.textColor, builder.selectedTextColor);
        monthPicker.setColors(builder.textColor, builder.selectedTextColor);
        dayPicker.setColors(builder.textColor, builder.selectedTextColor);
        hourPicker.setColors(builder.textColor, builder.selectedTextColor);
        minutePicker.setColors(builder.textColor, builder.selectedTextColor);
        //
        btnCancel.setText(builder.cancel);
        tvTitle.setText(builder.title);
        btnOK.setText(builder.ok);
        yearLabel.setText(builder.year);
        monthLabel.setText(builder.month);
        dayLabel.setText(builder.day);
        hourLabel.setText(builder.hour);
        minuteLabel.setText(builder.minute);
        //
        btnCancel.setTextColor(builder.cancelTextColor);
        tvTitle.setTextColor(builder.titleTextColor);
        btnOK.setTextColor(builder.okTextColor);
        yearLabel.setTextColor(builder.yearLabelTextColor);
        monthLabel.setTextColor(builder.monthLabelTextColor);
        dayLabel.setTextColor(builder.dayLabelTextColor);
        hourLabel.setTextColor(builder.hourLabelTextColor);
        minuteLabel.setTextColor(builder.minuteLabelTextColor);

        //
        curShowType = builder.showType;
        keepLastSelected = builder.keepLastSelected;
        showPickers(curShowType, builder.showYMDHMLabel);
        setIsLoop(builder.loopScroll);
    }

    private void initParameter() {
        startYear = startCalendar.get(Calendar.YEAR);
        startMonth = startCalendar.get(Calendar.MONTH) + 1;
        startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        startMinute = startCalendar.get(Calendar.MINUTE);
        endYear = endCalendar.get(Calendar.YEAR);
        endMonth = endCalendar.get(Calendar.MONTH) + 1;
        endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
        endHour = endCalendar.get(Calendar.HOUR_OF_DAY);
        endMinute = endCalendar.get(Calendar.MINUTE);
        selectedCalender.setTime(startCalendar.getTime());
    }

    private void initTimer() {
        initArrayList();
        //years
        for (int i = startYear; i <= endYear; i++) {
            years.add(String.valueOf(i));
        }

        //months
        for (int i = startMonth; i <= MAX_MONTH; i++) {
            months.add(formatTimeUnit(i));
        }

        //days
        int dayActualMaximum = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = startDay; i <= dayActualMaximum; i++) {
            days.add(formatTimeUnit(i));
        }

        //hours
        for (int i = startHour; i <= MAX_HOUR; i++) {
            hours.add(formatTimeUnit(i));
        }

        //minutes
        for (int i = startMinute; i <= MAX_MINUTE; i++) {
            minutes.add(formatTimeUnit(i));
        }
        loadComponent();
    }

    /**
     * 将“0-9”转换为“00-09”
     */
    private String formatTimeUnit(int unit) {
        return unit < 10 ? "0" + String.valueOf(unit) : String.valueOf(unit);
    }

    private void initArrayList() {
        if (years == null) years = new ArrayList<>();
        if (months == null) months = new ArrayList<>();
        if (days == null) days = new ArrayList<>();
        if (hours == null) hours = new ArrayList<>();
        if (minutes == null) minutes = new ArrayList<>();
        years.clear();
        months.clear();
        days.clear();
        hours.clear();
        minutes.clear();
    }

    private int findSelectedItemIndex(ShowType type, String value) {
        int index = -1;
        if (value == null || value.trim().length() == 0)
            return index;

        switch (type) {
            case YEAR:
                if (years != null && !years.isEmpty()){
                    for (int i = 0; i < years.size(); i++) {
                        if (value.equals(years.get(i))){
                            index = i;
                            break;
                        }
                    }
                }
                break;
            case MONTH:
                if (months != null && !months.isEmpty()){
                    for (int i = 0; i < months.size(); i++) {
                        if (value.equals(months.get(i))){
                            index = i;
                            break;
                        }
                    }
                }
                break;
            case DAY:
                if (days != null && !days.isEmpty()){
                    for (int i = 0; i < days.size(); i++) {
                        if (value.equals(days.get(i))){
                            index = i;
                            break;
                        }
                    }
                }
                break;
            case HOUR:
                if (hours != null && !hours.isEmpty()){
                    for (int i = 0; i < hours.size(); i++) {
                        if (value.equals(hours.get(i))){
                            index = i;
                            break;
                        }
                    }
                }
                break;
            case MINUTE:
                if (minutes != null && !minutes.isEmpty()){
                    for (int i = 0; i < minutes.size(); i++) {
                        if (value.equals(minutes.get(i))){
                            index = i;
                            break;
                        }
                    }
                }
                break;
            default:
                index = -1;
                break;
        }
        return index;
    }

    private void loadComponent() {
        yearPicker.setData(years);
        monthPicker.setData(months);
        dayPicker.setData(days);
        hourPicker.setData(hours);
        minutePicker.setData(minutes);
        yearPicker.setSelected(0);
        monthPicker.setSelected(0);
        dayPicker.setSelected(0);
        hourPicker.setSelected(0);
        minutePicker.setSelected(0);
        executeScroll();
    }

    private void addListener() {
        yearPicker.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.YEAR, Integer.parseInt(text));
                monthChange();
            }
        });

        monthPicker.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.DAY_OF_MONTH, 1);
                selectedCalender.set(Calendar.MONTH, Integer.parseInt(text) - 1);
                dayChange();
            }
        });

        dayPicker.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(text));
                hourChange();
            }
        });

        hourPicker.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(text));
                minuteChange();
            }
        });

        minutePicker.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.MINUTE, Integer.parseInt(text));
            }
        });
    }

    private void monthChange() {
        //只显示年份
        if (curShowType.value < ShowType.MONTH.value)
            return;

        months.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
        int newSelectedMonth;
        int newSelectedMonthIndex = 0;
        if (selectedYear == startYear) {
            newSelectedMonth = startMonth;
            for (int i = startMonth; i <= MAX_MONTH; i++) {
                months.add(formatTimeUnit(i));
                if (keepLastSelected && selectedMonth == i){
                    newSelectedMonth = i;
                    newSelectedMonthIndex = i - startMonth;
                }
            }
        } else if (selectedYear == endYear) {
            newSelectedMonth = 1;
            for (int i = 1; i <= endMonth; i++) {
                months.add(formatTimeUnit(i));
                if (keepLastSelected && selectedMonth == i){
                    newSelectedMonth = i;
                    newSelectedMonthIndex = i - 1;
                }
            }
        } else {
            newSelectedMonth = 1;
            for (int i = 1; i <= MAX_MONTH; i++) {
                months.add(formatTimeUnit(i));
                if (keepLastSelected && selectedMonth == i){
                    newSelectedMonth = i;
                    newSelectedMonthIndex = i - 1;
                }
            }
        }
        selectedCalender.set(Calendar.MONTH, newSelectedMonth - 1);
        monthPicker.setData(months);
        monthPicker.setSelected(newSelectedMonthIndex);
        executeAnimator(monthPicker);

        monthPicker.postDelayed(new Runnable() {
            @Override
            public void run() {
                dayChange();
            }
        }, 100);
    }

    private void dayChange() {
        //显示年、月
        if (curShowType.value < ShowType.DAY.value)
            return;

        days.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
        int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
        int newSelectedDay;
        int newSelectedDayIndex = 0;
        if (selectedYear == startYear && selectedMonth == startMonth) {
            newSelectedDay = startDay;
            for (int i = startDay; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                days.add(formatTimeUnit(i));
                if (keepLastSelected && selectedDay == i){
                    newSelectedDay = i;
                    newSelectedDayIndex = i - startDay;
                }
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth) {
            newSelectedDay = 1;
            for (int i = 1; i <= endDay; i++) {
                days.add(formatTimeUnit(i));
                if (keepLastSelected && selectedDay == i){
                    newSelectedDay = i;
                    newSelectedDayIndex = i - 1;
                }
            }
        } else {
            newSelectedDay = 1;
            for (int i = 1; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                days.add(formatTimeUnit(i));
                if (keepLastSelected && selectedDay == i){
                    newSelectedDay = i;
                    newSelectedDayIndex = i - 1;
                }
            }
        }
        selectedCalender.set(Calendar.DAY_OF_MONTH, newSelectedDay);
        dayPicker.setData(days);
        dayPicker.setSelected(newSelectedDayIndex);
        executeAnimator(dayPicker);

        dayPicker.postDelayed(new Runnable() {
            @Override
            public void run() {
                hourChange();
            }
        }, 100);
    }

    private void hourChange() {
        //显示年、月、日
        if (curShowType.value < ShowType.HOUR.value)
            return;

        hours.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
        int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
        int selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY);
        int newSelectedHour;
        int newSelectedHourIndex = 0;
        if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay) {
            newSelectedHour = startHour;
            for (int i = startHour; i <= MAX_HOUR; i++) {
                hours.add(formatTimeUnit(i));
                if (keepLastSelected && selectedHour == i){
                    newSelectedHour = i;
                    newSelectedHourIndex = i - startHour;
                }
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay) {
            newSelectedHour = MIN_HOUR;
            for (int i = MIN_HOUR; i <= endHour; i++) {
                hours.add(formatTimeUnit(i));
                if (keepLastSelected && selectedHour == i){
                    newSelectedHour = i;
                    newSelectedHourIndex = i - MIN_HOUR;
                }
            }
        } else {
            newSelectedHour = MIN_HOUR;
            for (int i = MIN_HOUR; i <= MAX_HOUR; i++) {
                hours.add(formatTimeUnit(i));
                if (keepLastSelected && selectedHour == i){
                    newSelectedHour = i;
                    newSelectedHourIndex = i - MIN_HOUR;
                }
            }
        }
        selectedCalender.set(Calendar.HOUR_OF_DAY, newSelectedHour);
        hourPicker.setData(hours);
        hourPicker.setSelected(newSelectedHourIndex);
        executeAnimator(hourPicker);

        hourPicker.postDelayed(new Runnable() {
            @Override
            public void run() {
                minuteChange();
            }
        }, 100);
    }

    private void minuteChange() {
        //显示年、月、日、时
        if (curShowType.value < ShowType.MINUTE.value)
            return;

        minutes.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
        int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
        int selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY);
        int selectedMinute = selectedCalender.get(Calendar.MINUTE);
        int newSelectedMinute;
        int newSelectedMinuteIndex = 0;
        if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay && selectedHour == startHour) {
            newSelectedMinute = startMinute;
            for (int i = startMinute; i <= MAX_MINUTE; i++) {
                minutes.add(formatTimeUnit(i));
                if (keepLastSelected && selectedHour == i){
                    newSelectedMinute = i;
                    newSelectedMinuteIndex = i - startMinute;
                }
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay && selectedHour == endHour) {
            newSelectedMinute = MIN_MINUTE;
            for (int i = MIN_MINUTE; i <= endMinute; i++) {
                minutes.add(formatTimeUnit(i));
                if (keepLastSelected && selectedHour == i){
                    newSelectedMinute = i;
                    newSelectedMinuteIndex = i - MIN_MINUTE;
                }
            }
        } else {
            newSelectedMinute = MIN_MINUTE;
            for (int i = MIN_MINUTE; i <= MAX_MINUTE; i++) {
                minutes.add(formatTimeUnit(i));
                if (keepLastSelected && selectedHour == i){
                    newSelectedMinute = i;
                    newSelectedMinuteIndex = i - MIN_MINUTE;
                }
            }
        }
        selectedCalender.set(Calendar.MINUTE, newSelectedMinute);
        minutePicker.setData(minutes);
        minutePicker.setSelected(newSelectedMinuteIndex);
        executeAnimator(minutePicker);
        executeScroll();
    }

    private void executeAnimator(View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(200).start();
    }

    private void executeScroll() {
        yearPicker.setCanScroll(years.size() > 1);
        monthPicker.setCanScroll(months.size() > 1);
        dayPicker.setCanScroll(days.size() > 1);
        hourPicker.setCanScroll(hours.size() > 1);
        minutePicker.setCanScroll(minutes.size() > 1);
    }

    public void show(Date time) {
        Date showDate = null;
        if (time.before(startCalendar.getTime())){
            showDate = startCalendar.getTime();
        } else if (time.after(endCalendar.getTime())){
            showDate = endCalendar.getTime();
        } else {
            showDate = time;
        }

        initParameter();
        initTimer();
        addListener();
        setSelectedTime(showDate);
        datePickerDialog.show();
    }

    /**
     * 设置日期控件显示部分
     */
    private void showPickers(ShowType showType, boolean showYMDHMLabel) {
        switch (showType) {
            case YEAR:
                yearPicker.setVisibility(View.VISIBLE);
                monthPicker.setVisibility(View.GONE);
                dayPicker.setVisibility(View.GONE);
                hourPicker.setVisibility(View.GONE);
                minutePicker.setVisibility(View.GONE);

                yearLabel.setVisibility(showYMDHMLabel ? View.VISIBLE : View.GONE);
                monthLabel.setVisibility(View.GONE);
                dayLabel.setVisibility(View.GONE);
                hourLabel.setVisibility(View.GONE);
                minuteLabel.setVisibility(View.GONE);
                break;
            case MONTH:
                yearPicker.setVisibility(View.VISIBLE);
                monthPicker.setVisibility(View.VISIBLE);
                dayPicker.setVisibility(View.GONE);
                hourPicker.setVisibility(View.GONE);
                minutePicker.setVisibility(View.GONE);

                yearLabel.setVisibility(showYMDHMLabel ? View.VISIBLE : View.GONE);
                monthLabel.setVisibility(showYMDHMLabel ? View.VISIBLE : View.GONE);
                dayLabel.setVisibility(View.GONE);
                hourLabel.setVisibility(View.GONE);
                minuteLabel.setVisibility(View.GONE);
                break;
            case DAY:
                yearPicker.setVisibility(View.VISIBLE);
                monthPicker.setVisibility(View.VISIBLE);
                dayPicker.setVisibility(View.VISIBLE);
                hourPicker.setVisibility(View.GONE);
                minutePicker.setVisibility(View.GONE);

                yearLabel.setVisibility(showYMDHMLabel ? View.VISIBLE : View.GONE);
                monthLabel.setVisibility(showYMDHMLabel ? View.VISIBLE : View.GONE);
                dayLabel.setVisibility(showYMDHMLabel ? View.VISIBLE : View.GONE);
                hourLabel.setVisibility(View.GONE);
                minuteLabel.setVisibility(View.GONE);
                break;
            case HOUR:
                yearPicker.setVisibility(View.VISIBLE);
                monthPicker.setVisibility(View.VISIBLE);
                dayPicker.setVisibility(View.VISIBLE);
                hourPicker.setVisibility(View.VISIBLE);
                minutePicker.setVisibility(View.GONE);

                yearLabel.setVisibility(showYMDHMLabel ? View.VISIBLE : View.GONE);
                monthLabel.setVisibility(showYMDHMLabel ? View.VISIBLE : View.GONE);
                dayLabel.setVisibility(showYMDHMLabel ? View.VISIBLE : View.GONE);
                hourLabel.setVisibility(showYMDHMLabel ? View.VISIBLE : View.GONE);
                minuteLabel.setVisibility(View.GONE);
                break;
            case MINUTE:
                yearPicker.setVisibility(View.VISIBLE);
                monthPicker.setVisibility(View.VISIBLE);
                dayPicker.setVisibility(View.VISIBLE);
                hourPicker.setVisibility(View.VISIBLE);
                minutePicker.setVisibility(View.VISIBLE);

                yearLabel.setVisibility(showYMDHMLabel ? View.VISIBLE : View.GONE);
                monthLabel.setVisibility(showYMDHMLabel ? View.VISIBLE : View.GONE);
                dayLabel.setVisibility(showYMDHMLabel ? View.VISIBLE : View.GONE);
                hourLabel.setVisibility(showYMDHMLabel ? View.VISIBLE : View.GONE);
                minuteLabel.setVisibility(showYMDHMLabel ? View.VISIBLE : View.GONE);
                break;
        }
    }

    /**
     * 设置日期控件是否可以循环滚动
     */
    private void setIsLoop(boolean isLoop) {
        this.yearPicker.setIsLoop(isLoop);
        this.monthPicker.setIsLoop(isLoop);
        this.dayPicker.setIsLoop(isLoop);
        this.hourPicker.setIsLoop(isLoop);
        this.minutePicker.setIsLoop(isLoop);
    }

    /**
     * 设置日期控件默认选中的时间
     *
     * @param time date
     */
    public void setSelectedTime(Date time) {
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTime(time);

        int y = tempCalendar.get(Calendar.YEAR);
        int m = tempCalendar.get(Calendar.MONTH) + 1;
        int d = tempCalendar.get(Calendar.DAY_OF_MONTH);
        int h = tempCalendar.get(Calendar.HOUR_OF_DAY);
        int mm = tempCalendar.get(Calendar.MINUTE);

        //years
        yearPicker.setSelected(String.valueOf(y));
        selectedCalender.set(Calendar.YEAR, y);

        //months
        if (curShowType.value >= ShowType.MONTH.value) {
            months.clear();
            //the same year
            if (startYear == endYear){
                for (int i = startMonth; i <= endMonth; i++) {
                    months.add(formatTimeUnit(i));
                }
            } else {
                if (y == startYear) {
                    for (int i = startMonth; i <= MAX_MONTH; i++) {
                        months.add(formatTimeUnit(i));
                    }
                } else if (y == endYear) {
                    for (int i = 1; i <= endMonth; i++) {
                        months.add(formatTimeUnit(i));
                    }
                } else {
                    for (int i = 1; i <= MAX_MONTH; i++) {
                        months.add(formatTimeUnit(i));
                    }
                }
            }
            monthPicker.setData(months);
            monthPicker.setSelected(formatTimeUnit(m));
            selectedCalender.set(Calendar.MONTH, m - 1);
            executeAnimator(monthPicker);
        }

        //days
        if (curShowType.value >= ShowType.DAY.value) {
            days.clear();
            if (y == startYear && m == startMonth) {
                for (int i = startDay; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    days.add(formatTimeUnit(i));
                }
            } else if (y == endYear && m == endMonth) {
                for (int i = 1; i <= endDay; i++) {
                    days.add(formatTimeUnit(i));
                }
            } else {
                for (int i = 1; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    days.add(formatTimeUnit(i));
                }
            }
            dayPicker.setData(days);
            dayPicker.setSelected(formatTimeUnit(d));
            selectedCalender.set(Calendar.DAY_OF_MONTH, d);
            executeAnimator(dayPicker);
        }

        //hours
        if (curShowType.value >= ShowType.HOUR.value) {
            hours.clear();
            int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
            if (y == startYear && m == startMonth && selectedDay == startDay) {
                for (int i = startHour; i <= MAX_HOUR; i++) {
                    hours.add(formatTimeUnit(i));
                }
            } else if (y == endYear && m == endMonth && selectedDay == endDay) {
                for (int i = MIN_HOUR; i <= endHour; i++) {
                    hours.add(formatTimeUnit(i));
                }
            } else {
                for (int i = MIN_HOUR; i <= MAX_HOUR; i++) {
                    hours.add(formatTimeUnit(i));
                }
            }
            hourPicker.setData(hours);
            hourPicker.setSelected(formatTimeUnit(h));
            selectedCalender.set(Calendar.HOUR_OF_DAY, h);
            executeAnimator(hourPicker);
        }

        //minutes
        if (curShowType.value >= ShowType.MINUTE.value) {
            minutes.clear();
            int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
            int selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY);
            if (y == startYear && m == startMonth && selectedDay == startDay && selectedHour == startHour) {
                for (int i = startMinute; i <= MAX_MINUTE; i++) {
                    minutes.add(formatTimeUnit(i));
                }
            } else if (y == endYear && m == endMonth && selectedDay == endDay && selectedHour == endHour) {
                for (int i = MIN_MINUTE; i <= endMinute; i++) {
                    minutes.add(formatTimeUnit(i));
                }
            } else {
                for (int i = MIN_MINUTE; i <= MAX_MINUTE; i++) {
                    minutes.add(formatTimeUnit(i));
                }
            }
            minutePicker.setData(minutes);
            minutePicker.setSelected(formatTimeUnit(mm));
            selectedCalender.set(Calendar.MINUTE, mm);
            executeAnimator(minutePicker);
        }

        executeScroll();
    }

    public static class Builder {
        CharSequence title;
        CharSequence cancel;
        CharSequence ok;
        CharSequence year;
        CharSequence month;
        CharSequence day;
        CharSequence hour;
        CharSequence minute;
        int titleTextColor;
        int cancelTextColor;
        int okTextColor;
        int yearLabelTextColor;
        int monthLabelTextColor;
        int dayLabelTextColor;
        int hourLabelTextColor;
        int minuteLabelTextColor;
        int textColor;
        int selectedTextColor;
        int headerBackgroundColor;
        int bodyBackgroundColor;
        int segmentingLineColor;
        int segmentingLineHeight;
        ShowType showType;//显示级别
        boolean keepLastSelected;//是否保留上一次的选择
        boolean showYMDHMLabel;//是否显示"年"，"月"，"日"，"时"，"分"
        boolean loopScroll;//是否循环滚动

        public Builder(Context context) {
            title = context.getString(R.string.title);
            cancel = context.getString(android.R.string.cancel);
            ok = context.getString(android.R.string.ok);
            year = context.getString(R.string.year);
            month = context.getString(R.string.month);
            day = context.getString(R.string.day);
            hour = context.getString(R.string.hour);
            minute = context.getString(R.string.minute);
            titleTextColor = 0xFF333333;
            cancelTextColor = 0xFF333333;
            okTextColor = 0xFF333333;
            yearLabelTextColor = 0xFF333333;
            monthLabelTextColor = 0xFF333333;
            dayLabelTextColor = 0xFF333333;
            hourLabelTextColor = 0xFF333333;
            minuteLabelTextColor = 0xFF333333;
            textColor = 0xFF333333;
            selectedTextColor = 0xFF59B29C;
            headerBackgroundColor = Color.WHITE;
            bodyBackgroundColor = Color.WHITE;
            segmentingLineColor = 0xFFC9C9C9;
            segmentingLineHeight = 1;
            showType = ShowType.MINUTE;
            keepLastSelected = false;
            showYMDHMLabel = true;
            loopScroll = false;
        }

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setCancel(CharSequence cancel) {
            this.cancel = cancel;
            return this;
        }

        public Builder setOk(CharSequence ok) {
            this.ok = ok;
            return this;
        }

        public Builder setYear(CharSequence year) {
            this.year = year;
            return this;
        }

        public Builder setMonth(CharSequence month) {
            this.month = month;
            return this;
        }

        public Builder setDay(CharSequence day) {
            this.day = day;
            return this;
        }

        public Builder setHour(CharSequence hour) {
            this.hour = hour;
            return this;
        }

        public Builder setMinute(CharSequence minute) {
            this.minute = minute;
            return this;
        }

        public Builder setTitleTextColor(@ColorInt int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public Builder setCancelTextColor(@ColorInt int cancelTextColor) {
            this.cancelTextColor = cancelTextColor;
            return this;
        }

        public Builder setOkTextColor(@ColorInt int okTextColor) {
            this.okTextColor = okTextColor;
            return this;
        }

        public Builder setYearLabelTextColor(@ColorInt int yearLabelTextColor) {
            this.yearLabelTextColor = yearLabelTextColor;
            return this;
        }

        public Builder setMonthLabelTextColor(@ColorInt int monthLabelTextColor) {
            this.monthLabelTextColor = monthLabelTextColor;
            return this;
        }

        public Builder setDayLabelTextColor(@ColorInt int dayLabelTextColor) {
            this.dayLabelTextColor = dayLabelTextColor;
            return this;
        }

        public Builder setHourLabelTextColor(@ColorInt int hourLabelTextColor) {
            this.hourLabelTextColor = hourLabelTextColor;
            return this;
        }

        public Builder setMinuteLabelTextColor(@ColorInt int minuteLabelTextColor) {
            this.minuteLabelTextColor = minuteLabelTextColor;
            return this;
        }

        public Builder setTextColor(@ColorInt int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder setSelectedTextColor(@ColorInt int selectedTextColor) {
            this.selectedTextColor = selectedTextColor;
            return this;
        }

        public Builder setHeaderBackgroundColor(@ColorInt int headerBackgroundColor) {
            this.headerBackgroundColor = headerBackgroundColor;
            return this;
        }

        public Builder setBodyBackgroundColor(@ColorInt int bodyBackgroundColor) {
            this.bodyBackgroundColor = bodyBackgroundColor;
            return this;
        }

        public Builder setSegmentingLineColor(@ColorInt int segmentingLineColor) {
            this.segmentingLineColor = segmentingLineColor;
            return this;
        }

        public Builder setSegmentingLineHeight(@IntRange(from = 1) int segmentingLineHeight) {
            this.segmentingLineHeight = segmentingLineHeight;
            return this;
        }

        public Builder setShowType(ShowType showType) {
            this.showType = showType;
            return this;
        }

        public Builder setKeepLastSelected(boolean keepLastSelected) {
            this.keepLastSelected = keepLastSelected;
            return this;
        }

        public Builder setShowYMDHMLabel(boolean showYMDHMLabel) {
            this.showYMDHMLabel = showYMDHMLabel;
            return this;
        }

        public Builder setLoopScroll(boolean loopScroll) {
            this.loopScroll = loopScroll;
            return this;
        }
    }
}
