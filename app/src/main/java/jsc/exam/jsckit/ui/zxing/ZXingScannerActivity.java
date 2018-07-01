package jsc.exam.jsckit.ui.zxing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.zxing.zxing.ui.ZXingFragment;

public class ZXingScannerActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_xing_scanner);
        setTitle(getClass().getSimpleName().replace("Activity", ""));

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new ZXingFragment()).commit();
    }

    public void widgetClick(View v) {

    }
}
