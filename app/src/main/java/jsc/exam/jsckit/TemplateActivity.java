package jsc.exam.jsckit;

import android.os.Bundle;
import android.support.annotation.Nullable;

import jsc.exam.jsckit.ui.BaseActivity;

public class TemplateActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        setTitle(getClass().getSimpleName().replace("Activity", ""));

    }
}
