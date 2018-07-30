package jsc.exam.jsckit.ui.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.itemlayout.JSCItemLayout;

public class JSCItemLayoutActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsc_item_layout);
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));

        JSCItemLayout layout = findViewById(R.id.item_layout);
        layout.getDotView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        layout.setUnreadCount(10);
    }

    public void widgetClick(View view){

    }
}
