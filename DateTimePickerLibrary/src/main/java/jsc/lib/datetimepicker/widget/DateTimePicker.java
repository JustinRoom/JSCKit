package jsc.lib.datetimepicker.widget;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import jsc.lib.datetimepicker.R;

/**
 * <p>
 *     日期时间选择器
 *     <br/>本库是基于<a href="https://github.com/liuwan1992/CustomDatePicker" target="_blank">https://github.com/liuwan1992/CustomDatePicker</a>上的修改提炼而成。
 *     <br/><br/>非常感谢作者<a href="https://github.com/liuwan1992" target=""><h3>liuwan1992</h3></a>
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

    public enum SCROLL_TYPE {
        HOUR(1),
        MINUTE(2);

        SCROLL_TYPE(int value) {
            this.value = value;
        }

        public int value;
    }

    private int scrollUnits = SCROLL_TYPE.HOUR.value + SCROLL_TYPE.MINUTE.value;
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

    private ArrayList<String> year, month, day, hour, minute;
    private int startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute;
    private boolean spanYear, spanMon, spanDay, spanHour, spanMin;
    private Calendar selectedCalender, startCalendar, endCalendar;

    public DateTimePicker(Context context, ResultHandler resultHandler, Date startDate, Date endDate) {
        this(context, resultHandler, startDate, endDate, null);
    }

    public DateTimePicker(Context context, ResultHandler resultHandler, Date startDate, Date endDate, Builder builder) {
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

    private void updateView(Builder builder){
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
        showSpecificTime(builder.showTime);
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
        spanYear = startYear != endYear;
        spanMon = (!spanYear) && (startMonth != endMonth);
        spanDay = (!spanMon) && (startDay != endDay);
        spanHour = (!spanDay) && (startHour != endHour);
        spanMin = (!spanHour) && (startMinute != endMinute);
        selectedCalender.setTime(startCalendar.getTime());
    }

    private void initTimer() {
        initArrayList();
        if (spanYear) {
            for (int i = startYear; i <= endYear; i++) {
                year.add(String.valueOf(i));
            }
            for (int i = startMonth; i <= MAX_MONTH; i++) {
                month.add(formatTimeUnit(i));
            }
            for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(formatTimeUnit(i));
            }

            if ((scrollUnits & SCROLL_TYPE.HOUR.value) != SCROLL_TYPE.HOUR.value) {
                hour.add(formatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= MAX_HOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAX_MINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
        } else if (spanMon) {
            year.add(String.valueOf(startYear));
            for (int i = startMonth; i <= endMonth; i++) {
                month.add(formatTimeUnit(i));
            }
            for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(formatTimeUnit(i));
            }

            if ((scrollUnits & SCROLL_TYPE.HOUR.value) != SCROLL_TYPE.HOUR.value) {
                hour.add(formatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= MAX_HOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAX_MINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
        } else if (spanDay) {
            year.add(String.valueOf(startYear));
            month.add(formatTimeUnit(startMonth));
            for (int i = startDay; i <= endDay; i++) {
                day.add(formatTimeUnit(i));
            }

            if ((scrollUnits & SCROLL_TYPE.HOUR.value) != SCROLL_TYPE.HOUR.value) {
                hour.add(formatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= MAX_HOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAX_MINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
        } else if (spanHour) {
            year.add(String.valueOf(startYear));
            month.add(formatTimeUnit(startMonth));
            day.add(formatTimeUnit(startDay));

            if ((scrollUnits & SCROLL_TYPE.HOUR.value) != SCROLL_TYPE.HOUR.value) {
                hour.add(formatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= endHour; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAX_MINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
        } else if (spanMin) {
            year.add(String.valueOf(startYear));
            month.add(formatTimeUnit(startMonth));
            day.add(formatTimeUnit(startDay));
            hour.add(formatTimeUnit(startHour));

            if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= endMinute; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
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
        if (year == null) year = new ArrayList<>();
        if (month == null) month = new ArrayList<>();
        if (day == null) day = new ArrayList<>();
        if (hour == null) hour = new ArrayList<>();
        if (minute == null) minute = new ArrayList<>();
        year.clear();
        month.clear();
        day.clear();
        hour.clear();
        minute.clear();
    }

    private void loadComponent() {
        yearPicker.setData(year);
        monthPicker.setData(month);
        dayPicker.setData(day);
        hourPicker.setData(hour);
        minutePicker.setData(minute);
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
        month.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        if (selectedYear == startYear) {
            for (int i = startMonth; i <= MAX_MONTH; i++) {
                month.add(formatTimeUnit(i));
            }
        } else if (selectedYear == endYear) {
            for (int i = 1; i <= endMonth; i++) {
                month.add(formatTimeUnit(i));
            }
        } else {
            for (int i = 1; i <= MAX_MONTH; i++) {
                month.add(formatTimeUnit(i));
            }
        }
        selectedCalender.set(Calendar.MONTH, Integer.parseInt(month.get(0)) - 1);
        monthPicker.setData(month);
        monthPicker.setSelected(0);
        executeAnimator(monthPicker);

        monthPicker.postDelayed(new Runnable() {
            @Override
            public void run() {
                dayChange();
            }
        }, 100);
    }

    private void dayChange() {
        day.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
        if (selectedYear == startYear && selectedMonth == startMonth) {
            for (int i = startDay; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(formatTimeUnit(i));
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth) {
            for (int i = 1; i <= endDay; i++) {
                day.add(formatTimeUnit(i));
            }
        } else {
            for (int i = 1; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(formatTimeUnit(i));
            }
        }
        selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.get(0)));
        dayPicker.setData(day);
        dayPicker.setSelected(0);
        executeAnimator(dayPicker);

        dayPicker.postDelayed(new Runnable() {
            @Override
            public void run() {
                hourChange();
            }
        }, 100);
    }

    private void hourChange() {
        if ((scrollUnits & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value) {
            hour.clear();
            int selectedYear = selectedCalender.get(Calendar.YEAR);
            int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
            int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
            if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay) {
                for (int i = startHour; i <= MAX_HOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay) {
                for (int i = MIN_HOUR; i <= endHour; i++) {
                    hour.add(formatTimeUnit(i));
                }
            } else {
                for (int i = MIN_HOUR; i <= MAX_HOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }
            selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour.get(0)));
            hourPicker.setData(hour);
            hourPicker.setSelected(0);
            executeAnimator(hourPicker);
        }

        hourPicker.postDelayed(new Runnable() {
            @Override
            public void run() {
                minuteChange();
            }
        }, 100);
    }

    private void minuteChange() {
        if ((scrollUnits & SCROLL_TYPE.MINUTE.value) == SCROLL_TYPE.MINUTE.value) {
            minute.clear();
            int selectedYear = selectedCalender.get(Calendar.YEAR);
            int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
            int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
            int selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY);
            if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay && selectedHour == startHour) {
                for (int i = startMinute; i <= MAX_MINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay && selectedHour == endHour) {
                for (int i = MIN_MINUTE; i <= endMinute; i++) {
                    minute.add(formatTimeUnit(i));
                }
            } else {
                for (int i = MIN_MINUTE; i <= MAX_MINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
            selectedCalender.set(Calendar.MINUTE, Integer.parseInt(minute.get(0)));
            minutePicker.setData(minute);
            minutePicker.setSelected(0);
            executeAnimator(minutePicker);
        }
        executeScroll();
    }

    private void executeAnimator(View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(200).start();
    }

    private void executeScroll() {
        yearPicker.setCanScroll(year.size() > 1);
        monthPicker.setCanScroll(month.size() > 1);
        dayPicker.setCanScroll(day.size() > 1);
        hourPicker.setCanScroll(hour.size() > 1 && (scrollUnits & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value);
        minutePicker.setCanScroll(minute.size() > 1 && (scrollUnits & SCROLL_TYPE.MINUTE.value) == SCROLL_TYPE.MINUTE.value);
    }

    private int disScrollUnit(SCROLL_TYPE... scroll_types) {
        if (scroll_types == null || scroll_types.length == 0) {
            scrollUnits = SCROLL_TYPE.HOUR.value + SCROLL_TYPE.MINUTE.value;
        } else {
            for (SCROLL_TYPE scroll_type : scroll_types) {
                scrollUnits ^= scroll_type.value;
            }
        }
        return scrollUnits;
    }

    public void show(Date time) {
        if (startCalendar.getTime().getTime() < endCalendar.getTime().getTime()) {
            initParameter();
            initTimer();
            addListener();
            setSelectedTime(time);
            datePickerDialog.show();
        }
    }

    /**
     * 设置日期控件是否显示时和分
     */
    private void showSpecificTime(boolean show) {
        if (show) {
            disScrollUnit();
            hourPicker.setVisibility(View.VISIBLE);
            hourLabel.setVisibility(View.VISIBLE);
            minutePicker.setVisibility(View.VISIBLE);
            minuteLabel.setVisibility(View.VISIBLE);
        } else {
            disScrollUnit(SCROLL_TYPE.HOUR, SCROLL_TYPE.MINUTE);
            hourPicker.setVisibility(View.GONE);
            hourLabel.setVisibility(View.GONE);
            minutePicker.setVisibility(View.GONE);
            minuteLabel.setVisibility(View.GONE);
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
     */
    public void setSelectedTime(Date time) {
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTime(time);

        int y = tempCalendar.get(Calendar.YEAR);
        int m = tempCalendar.get(Calendar.MONTH) + 1;
        int d = tempCalendar.get(Calendar.DAY_OF_MONTH);
        int h = tempCalendar.get(Calendar.HOUR_OF_DAY);
        int mm = tempCalendar.get(Calendar.MINUTE);

        //year
        yearPicker.setSelected(String.valueOf(y));
        selectedCalender.set(Calendar.YEAR, y);

        //month
        month.clear();
        if (y == startYear) {
            for (int i = startMonth; i <= MAX_MONTH; i++) {
                month.add(formatTimeUnit(i));
            }
        } else if (y == endYear) {
            for (int i = 1; i <= endMonth; i++) {
                month.add(formatTimeUnit(i));
            }
        } else {
            for (int i = 1; i <= MAX_MONTH; i++) {
                month.add(formatTimeUnit(i));
            }
        }
        monthPicker.setData(month);
        monthPicker.setSelected(formatTimeUnit(m));
        selectedCalender.set(Calendar.MONTH, m - 1);
        executeAnimator(monthPicker);

        //day
        day.clear();
        if (y == startYear && m == startMonth) {
            for (int i = startDay; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(formatTimeUnit(i));
            }
        } else if (y == endYear && m == endMonth) {
            for (int i = 1; i <= endDay; i++) {
                day.add(formatTimeUnit(i));
            }
        } else {
            for (int i = 1; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(formatTimeUnit(i));
            }
        }
        dayPicker.setData(day);
        dayPicker.setSelected(formatTimeUnit(d));
        selectedCalender.set(Calendar.DAY_OF_MONTH, d);
        executeAnimator(dayPicker);

        //hour
        if ((scrollUnits & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value) {
            hour.clear();
            int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
            if (y == startYear && m == startMonth && selectedDay == startDay) {
                for (int i = startHour; i <= MAX_HOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            } else if (y == endYear && m == endMonth && selectedDay == endDay) {
                for (int i = MIN_HOUR; i <= endHour; i++) {
                    hour.add(formatTimeUnit(i));
                }
            } else {
                for (int i = MIN_HOUR; i <= MAX_HOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }
            hourPicker.setData(hour);
            hourPicker.setSelected(formatTimeUnit(h));
            selectedCalender.set(Calendar.HOUR_OF_DAY, h);
            executeAnimator(hourPicker);
        }

        //minute
        if ((scrollUnits & SCROLL_TYPE.MINUTE.value) == SCROLL_TYPE.MINUTE.value) {
            minute.clear();
            int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
            int selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY);
            if (y == startYear && m == startMonth && selectedDay == startDay && selectedHour == startHour) {
                for (int i = startMinute; i <= MAX_MINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            } else if (y == endYear && m == endMonth && selectedDay == endDay && selectedHour == endHour) {
                for (int i = MIN_MINUTE; i <= endMinute; i++) {
                    minute.add(formatTimeUnit(i));
                }
            } else {
                for (int i = MIN_MINUTE; i <= MAX_MINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
            minutePicker.setData(minute);
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
        boolean showTime;
        boolean loopScroll;

        public Builder(Context context){
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
            showTime = true;
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

        public Builder setShowTime(boolean showTime) {
            this.showTime = showTime;
            return this;
        }

        public Builder setLoopScroll(boolean loopScroll) {
            this.loopScroll = loopScroll;
            return this;
        }
    }
}
