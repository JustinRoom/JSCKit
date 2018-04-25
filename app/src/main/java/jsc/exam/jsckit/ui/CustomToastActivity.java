package jsc.exam.jsckit.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import jsc.exam.jsckit.R;

public class CustomToastActivity extends ABaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_toast);
        setTitle(getClass().getSimpleName().replace("Activity", ""));
    }

    public void widgetClick(View v){
        int version = Build.VERSION.SDK_INT;
        showCustomToast("Hello, I am a custom toast." + version);
    }
}
