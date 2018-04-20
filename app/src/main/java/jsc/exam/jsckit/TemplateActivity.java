package jsc.exam.jsckit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import jsc.exam.jsckit.ui.ABaseActivity;

public class TemplateActivity extends ABaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        setTitle(getClass().getSimpleName().replace("Activity", ""));
    }
}
