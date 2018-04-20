package jsc.exam.jsckit.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import jsc.exam.jsckit.R;
import jsc.lib.datetimepicker.widget.DateTimePicker;

public class DateTimePickerActivity extends ABaseActivity {

    private TextView currentDate, currentTime;
    private DateTimePicker dateTimePicker1, dateTimePicker2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time_picker);
        setTitle(getClass().getSimpleName().replace("Activity", ""));

        currentDate = findViewById(R.id.currentDate);
        currentTime = findViewById(R.id.currentTime);
        initDatePicker();
    }

    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.selectDate:
                // 日期格式为yyyy-MM-dd
                dateTimePicker1.show(date1);
                break;

            case R.id.selectTime:
                // 日期格式为yyyy-MM-dd HH:mm
                dateTimePicker2.show(date2);
                break;
        }
    }

    SimpleDateFormat format1, format2;
    Date date1, date2;
    private void initDatePicker() {
        format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        format2 = new SimpleDateFormat("yyyy-MM-dd  HH:mm", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        date2 = date1 = calendar.getTime();
        currentDate.setText(format1.format(date1));
        currentTime.setText(format2.format(date1));

        calendar.set(Calendar.YEAR, 2010);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.YEAR, 2030);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        Date endDate = calendar.getTime();


        DateTimePicker.Builder builder = new DateTimePicker.Builder(this)
                .setCancelTextColor(Color.RED)
                .setOkTextColor(getResources().getColor(R.color.colorPrimary))
                .setTitleTextColor(0xFF999999)
                .setSelectedTextColor(getResources().getColor(R.color.colorAccent));
        dateTimePicker1 = new DateTimePicker(this, new DateTimePicker.ResultHandler() {
            @Override
            public void handle(Date date) {
                DateTimePickerActivity.this.date1 = date;
                currentDate.setText(format1.format(date));
            }
        }, startDate, endDate, builder); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        dateTimePicker1.showSpecificTime(false); // 不显示时和分
        dateTimePicker1.setIsLoop(false); // 不允许循环滚动

        dateTimePicker2 = new DateTimePicker(this, new DateTimePicker.ResultHandler() {
            @Override
            public void handle(Date date) {
                date2 = date;
                currentTime.setText(format2.format(date));
            }
        }, startDate, endDate); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        dateTimePicker2.showSpecificTime(true); // 显示时和分
        dateTimePicker2.setIsLoop(true); // 允许循环滚动
    }
}
