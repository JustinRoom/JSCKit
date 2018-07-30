package jsc.exam.jsckit.ui.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.itemlayout.JSCItemLayout;
import jsc.kit.component.widget.DotView;

public class JSCItemLayoutActivity extends BaseActivity {

    DotView dotView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsc_item_layout);
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));

        JSCItemLayout layout = findViewById(R.id.item_layout_02);
        layout.getDotView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        layout.setUnreadCount(10);

        dotView = findViewById(R.id.dot_view);
        dotView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dotView.setShape(DotView.CIRCULAR);
            }
        });
    }

    public void widgetClick(View view){
        switch (view.getId()){
            case R.id.item_layout_01:
                dotView.setShape(DotView.ROUND_CORNER_SQUARE, 4);
                break;
            case R.id.item_layout_02:
                dotView.setShape(DotView.TRIANGLE, 4);
                break;
        }
    }
}
