package jsc.kit.datetimepicker.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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
public class YearMonthPickerDialog extends TwoPickerDialog {

    private Date selectedDate;
    private List<Integer> years = new ArrayList<>();
    private List<Integer> months = new ArrayList<>();
    private List<String> yearArray = new ArrayList<>();
    private List<String> monthArray = new ArrayList<>();
    private int startYear, endYear;
    private int startMonth, endMonth;

    public YearMonthPickerDialog(Context context) {
        super(context);
    }

    public void init(@NonNull Date startDate, @NonNull Date endDate){
        init(startDate, endDate, new Date(startDate.getTime()));
    }

    public void init(@NonNull Date startDate, @NonNull Date endDate, @NonNull Date selectedDate){
        if (startDate.after(endDate))
            throw new IllegalArgumentException("start date is after end date.");

        if (selectedDate.before(startDate) || selectedDate.after(endDate))
            throw new IllegalArgumentException("selected date must between start date and end date.");

        this.selectedDate = selectedDate;
        years.clear();
        months.clear();
        yearArray.clear();
        monthArray.clear();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        startYear = calendar.get(Calendar.YEAR);
        startMonth = calendar.get(Calendar.MONTH) + 1;
        calendar.setTime(endDate);
        endYear = calendar.get(Calendar.YEAR);
        endMonth = calendar.get(Calendar.MONTH) + 1;
        calendar.setTime(selectedDate);
        int selectedYear = calendar.get(Calendar.YEAR);
        int selectedMonth = calendar.get(Calendar.MONTH) + 1;

        for (int i = startYear; i <= endYear; i++) {
            years.add(i);
            yearArray.add(i + "年");
        }

        if (startYear == endYear){
            for (int i = startMonth; i <= endMonth; i++) {
                months.add(i);
                monthArray.add(i + "月");
            }
        } else {
            if (selectedYear == startYear){
                for (int i = startMonth; i <= 12; i++) {
                    months.add(i);
                    monthArray.add(i + "月");
                }
            } else if (selectedYear == endYear){
                for (int i = 1 ; i <= endMonth ; i++) {
                    months.add(i);
                    monthArray.add(i + "月");
                }
            } else {
                for (int i = 1; i <= 12; i++) {
                    months.add(i);
                    monthArray.add(i + "月");
                }
            }
        }
        setItems1(yearArray);
        setItems2(monthArray);
        setSelected1(findSelectedIndexByValue(0, selectedYear));
        setSelected2(findSelectedIndexByValue(1, selectedMonth));
    }

    /**
     *
     * @param key 0year  1month
     * @param value value
     * @return index
     */
    private int findSelectedIndexByValue(int key, int value){
        int index = 0;
        switch (key){
            case 0:
                for (int i = 0; i < years.size(); i++) {
                    if (value == years.get(i)){
                        index = i;
                        break;
                    }
                }
                break;
            case 1:
                for (int i = 0; i < months.size(); i++) {
                    if (value == months.get(i)){
                        index = i;
                        break;
                    }
                }
                break;
        }
        return index;
    }

    @Override
    void initView(LinearLayout pickerContainer) {
        super.initView(pickerContainer);
        pickerContainer.setWeightSum(5);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (selectedResultHandler != null)
                    selectedResultHandler.handleSelectedResult(selectedDate);
            }
        });
    }

    @Override
    protected void onSelectChange(int key, int selectedIndex, String selectedItem) {
        //year
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        int lasSelectedYear = calendar.get(Calendar.YEAR);
        if (key == 0){
            int selectedYear = years.get(selectedIndex);
            calendar.set(Calendar.YEAR, selectedYear);

            int lastSelectedMonthIndex = getSelected2();
            int lastSelectedMonth = months.get(lastSelectedMonthIndex);
            //如果选中的年份不同于上一次选中的年份，则更新月份列表
            if (selectedYear != lasSelectedYear){
                months.clear();
                monthArray.clear();
                if (selectedYear == startYear){
                    for (int i = startMonth; i <= 12; i++) {
                        months.add(i);
                        monthArray.add(i + "月");
                    }
                } else if (selectedYear == endYear){
                    for (int i = 1 ; i <= endMonth ; i++) {
                        months.add(i);
                        monthArray.add(i + "月");
                    }
                } else {
                    for (int i = 1; i <= 12; i++) {
                        months.add(i);
                        monthArray.add(i + "月");
                    }
                }
                setItems2(monthArray);
                int monthSelectedIndex = findSelectedIndexByValue(1, lastSelectedMonth);
                setSelected2(monthSelectedIndex);
                calendar.set(Calendar.MONTH, months.get(monthSelectedIndex) - 1);

                if (lastSelectedMonth != months.get(monthSelectedIndex))
                    executeAnimator(wheelView2);
            }
        }
        //month
        else {
            calendar.set(Calendar.MONTH, months.get(selectedIndex) - 1);
        }
        selectedDate = calendar.getTime();
        calendar.setTime(selectedDate);
        Log.i("YearMonthPickerDialog", "onSelectChange: [" + calendar.get(Calendar.YEAR) + ", " + (calendar.get(Calendar.MONTH) + 1) + "]");
    }

    @Override
    public void show() {
        executeAnimator(wheelView1);
        wheelView1.postDelayed(new Runnable() {
            @Override
            public void run() {
                executeAnimator(wheelView2);
            }
        }, 100);
        super.show();
    }

    public interface OnSelectedResultHandler extends TwoPickerDialog.OnSelectedResultHandler {
        void handleSelectedResult(@NonNull Date date);
    }
}
