package jsc.exam.jsckit.ui.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.widget.AutoTextSizeView;

public class AutoSizeTextViewActivity extends BaseActivity {

    AutoTextSizeView autoTextSizeView;
    View myView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_size_text_view);
        setTitle(getClass().getSimpleName().replace("Activity", ""));

        autoTextSizeView = findViewById(R.id.auto_text_size_view);
        myView = findViewById(R.id.rv);
    }

    String[] text = {"hello, world", "Hello", "Kit"};
    int[] widths = {100, 64, 128, 48};
    public void onWidgetClick(View view){
        switch (view.getId()){
            case R.id.btn_change_text:
                autoTextSizeView.setAutoSizeImmediatelyText(text[new Random().nextInt(3)]);
                break;
            case R.id.btn_change_width:
                int dp = widths[new Random().nextInt(4)];
                int pix = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
                ViewGroup.LayoutParams params = autoTextSizeView.getLayoutParams();
                params.width = pix;
                autoTextSizeView.setLayoutParams(params);

                ViewGroup.LayoutParams p = myView.getLayoutParams();
                p.width = pix;
                myView.setLayoutParams(p);
                break;
        }
    }
}
