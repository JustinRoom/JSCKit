package jsc.exam.jsckit.ui.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.BaseActivity;

public class JSCItemLayoutActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsc_item_layout);
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));
    }

    public void widgetClick(View view){

    }
}
