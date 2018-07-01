package jsc.exam.jsckit.ui.component;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.vscrollscreen.VScrollScreenLayout;

public class VScrollScreenLayoutActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v_scroll_screen_layout);
        setTitle(getClass().getSimpleName().replace("Activity", ""));

        VScrollScreenLayout scrollScreenLayout = findViewById(R.id.v_scroll_screen_layout);
        scrollScreenLayout.setOnScrollPageChangedListener(new VScrollScreenLayout.OnScrollPageChangedListener() {
            @Override
            public void onScroll(Context context, int pageIndex) {
                showCustomToast("The current page is " + (pageIndex + 1));
            }
        });
    }

    public void widgetClick(View v){

    }
}
