package jsc.exam.jsckit.ui.component;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.monthview.DayItem;
import jsc.kit.component.monthview.DayView;
import jsc.kit.component.monthview.MonthUtils;
import jsc.kit.component.monthview.MonthView;

public class MonthViewActivity extends BaseActivity {

    MonthView monthView;
    TextView tvYearMonth;
    int year;
    int month;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_view);
        setTitle(getClass().getSimpleName().replace("Activity", ""));

        initView();
    }

    private void initView() {
        monthView = findViewById(R.id.month_view);
        tvYearMonth = findViewById(R.id.tv_year_month);
        monthView.setOnDayClickListener(new MonthView.OnDayClickListener() {
            @Override
            public void onDayClick(DayView dayView) {

            }
        });
        monthView.setOnDayLongClickListener(new MonthView.OnDayLongClickListener() {
            @Override
            public boolean onDayLongClick(DayView dayView) {
                return false;
            }
        });
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        tvYearMonth.setText(year + "-" + month);
        initMonthView(year, month);
    }

    private void initMonthView(int year, int month){
        List<DayItem> dayItems = MonthUtils.getMonthDays(year, month);
        for (int i = 0; i < dayItems.size(); i++) {
            boolean enable = new Random().nextBoolean();
            DayItem item = dayItems.get(i);
            switch (item.getKey()){
                case -1://上个月的最后几天
                case 1://下个月的前几天
                    item.setBackground(R.drawable.circle_gray_light_shape);
                    break;
                case 0://本月的所有天数
                    item.setBackground(R.drawable.circle_theme_light_shape);
                    break;
            }
            item.setSubLabel(enable ? "可约" : "不可约");
            item.setSubLabelTextColor(enable ? Color.YELLOW : 0xFF666666);
        }
        monthView.setDays(dayItems);
    }

    public void onWidgetClick(View view){
        switch (view.getId()){
            case R.id.btn_pre:
                month --;
                if (month <= 0){
                    month = 12;
                    year --;
                }
                break;
            case R.id.btn_next:
                month ++;
                if (month > 12){
                    month = 1;
                    year ++;
                }
                break;
        }
        tvYearMonth.setText(year + "-" + month);
        initMonthView(year, month);
    }
}
