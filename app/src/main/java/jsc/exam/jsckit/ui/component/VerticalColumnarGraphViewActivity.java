package jsc.exam.jsckit.ui.component;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.graph.ColumnarItem;
import jsc.kit.component.graph.VerticalColumnarGraphView;
import jsc.kit.datetimepicker.widget.Builder;
import jsc.kit.datetimepicker.widget.TwoPickerDialog;
import jsc.kit.datetimepicker.widget.YearMonthPickerDialog;

public class VerticalColumnarGraphViewActivity extends BaseActivity {

    VerticalColumnarGraphView verticalColumnarGraphView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_columnar_graph_view);
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));
        verticalColumnarGraphView = findViewById(R.id.vertical_columnar_graph);

        handlerProvider.sendUIEmptyMessageDelay(0, 350);
    }

    @Override
    public void handleUIMessage(Message msg) {
        super.handleUIMessage(msg);
        verticalColumnarGraphView.initCustomUI(
                new jsc.kit.component.graph.Builder()
                        .setYAxisLabels(new String[]{"0", "25", "50", "75", "100", "125"})
                        .setOffset(60, 0, 20, 20)
        );
        verticalColumnarGraphView.setItems(createTestData());
    }

    /**
     * 创建测试数据
     *
     * @return test data source
     */
    private List<ColumnarItem> createTestData() {
        List<ColumnarItem> data = new ArrayList<>();
        float[] ratios = {.76f, .36f, .54f, .36f, .6f, .36f, .6f};
        int[] colors = {0xFFFFCF5E, 0xFFB4EE4D, 0xFF27E67B, 0xFF36C771, 0xFF1CA291, 0xFF24DDD0, 0xFf32CEF7};
        String[] labels = {"返情配种", "多次输精", "人工授精", "本交", "同精液配种", "已配种母猪", "其他配种"};
        String[] values = {"20头", "20头", "20头", "20头", "20头", "20头", "20头"};
        for (int i = 0; i < 7; i++) {
            ColumnarItem item = new ColumnarItem();
            item.setColor(colors[i]);
            item.setRatio(ratios[i]);
            item.setLabel(labels[i]);
            item.setValue(values[i]);
            data.add(item);
        }
        return data;
    }

    public void widgetClick(View view) {
        showDialog();
//        showTwoPickerDialog();
    }

    YearMonthPickerDialog dialog;

    private void showDialog() {
        if (dialog == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2010);
            calendar.set(Calendar.MONTH, 4);
            Date startDate = calendar.getTime();
            calendar.set(Calendar.YEAR, 2032);
            calendar.set(Calendar.MONTH, 10);
            Date endDate = calendar.getTime();

            Builder builder = new Builder();
            builder.setTitle("选择年月");
            builder.setLeftText("取消");
            builder.setRightText("确定");
            builder.setTextColor(0xFFCCCCCC);
            builder.setMinTextAlpha(0xFf);
            dialog = new YearMonthPickerDialog(this);
            dialog.setSelectedResultHandler(new TwoPickerDialog.OnSelectedResultHandler() {
                @Override
                public void handleSelectedResult(int index1, String item1, int index2, String item2) {

                }

                @Override
                public void handleSelectedResult(@NonNull Date date) {
                    showToast(new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(date));
                }
            });
            dialog.updateUI(builder);

            dialog.init(startDate, endDate);
        }

        dialog.show();
    }

    TwoPickerDialog twoPickerDialog;

    private void showTwoPickerDialog() {
        if (twoPickerDialog == null) {
            List<String> years = new ArrayList<>();
            for (int i = 2010; i <= 2032; i++) {
                years.add(i + "年");
            }

            List<String> months = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                months.add(i + "月");
            }

            twoPickerDialog = new TwoPickerDialog(this);
            twoPickerDialog.setLoop1(false);
            twoPickerDialog.setLoop2(false);
            twoPickerDialog.setItems1(years);
            twoPickerDialog.setItems2(months);
            twoPickerDialog.setSelected1(0);
            twoPickerDialog.setSelected2(0);
        }

        twoPickerDialog.show();
    }
}
