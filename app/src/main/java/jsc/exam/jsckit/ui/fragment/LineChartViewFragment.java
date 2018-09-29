package jsc.exam.jsckit.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

import jsc.exam.jsckit.R;
import jsc.kit.component.graph.ColumnarItem;
import jsc.kit.component.graph.LabelItem;
import jsc.kit.component.graph.LineChartView;

public class LineChartViewFragment extends Fragment {

    LineChartView lineChartView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_line_chart_view, container, false);
        lineChartView = root.findViewById(R.id.line_chart_view);
        lineChartView.applyTestData();
        root.findViewById(R.id.btn_show12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] labels = new String[]{"", "15%", "30%", "45%", "60%", "75%", "90%", "105%"};
                LabelItem[] yLabels = new LabelItem[labels.length];
                for (int i = 0; i < labels.length; i++) {
                    LabelItem item = new LabelItem();
                    item.setLabel(labels[i]);
                    yLabels[i] = item;
                }

                int size = 12;
                LabelItem[] xLabels = new LabelItem[size];
                for (int i = 0; i < xLabels.length; i++) {
                    LabelItem item = new LabelItem();
                    item.setLabel((i + 1) + "月");
                    xLabels[i] = item;
                }

                ColumnarItem[] data = new ColumnarItem[size];
                Random random = new Random();
                for (int i = 0; i < data.length; i++) {
                    ColumnarItem item = new ColumnarItem();
                    item.setRatio(random.nextFloat());
                    data[i] = item;
                }
//                lineChartView.setXLabels(xLabels);
//                lineChartView.setYLabels(yLabels);
                lineChartView.setData(data);
            }
        });
        root.findViewById(R.id.btn_show_grid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineChartView.toggleDashGrid();
            }
        });
        return root;
    }
}
