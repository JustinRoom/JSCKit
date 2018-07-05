package jsc.exam.jsckit.ui.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.SeekBar;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.archeaderview.BaseArcHeaderView;
import jsc.kit.component.archeaderview.LGradientArcHeaderView;
import jsc.kit.component.archeaderview.PictureArcHeaderView;

public class ArcHeaderViewActivity extends BaseActivity {

    LGradientArcHeaderView lGradientArcHeaderView;
    PictureArcHeaderView pictureArcHeaderView;
    SeekBar seekBar;
    int maxArcHeight = 200;
    int type = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_header_view);
        setTitleBarTitle(getClass().getSimpleName().replace("Activity", ""));

        lGradientArcHeaderView = findViewById(R.id.lg_header_view);
        pictureArcHeaderView = findViewById(R.id.p_header_view);
        seekBar = findViewById(R.id.seek_bar);

        findViewById(R.id.fy_ar_p).setVisibility(View.GONE);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int arcHeight = maxArcHeight * progress / seekBar.getMax();
                lGradientArcHeaderView.setArcHeight(arcHeight);
                pictureArcHeaderView.setArcHeight(arcHeight);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setProgress(50);
    }

    public void widgetClick(View view) {
        switch (view.getId()) {
            case R.id.btn_type_lg:
                if (type == 0)
                    return;
                type = 0;
                findViewById(R.id.fy_ar_lg).setVisibility(View.VISIBLE);
                findViewById(R.id.fy_ar_p).setVisibility(View.GONE);
                break;
            case R.id.btn_type_pic:
                if (type == 1)
                    return;
                type = 1;
                findViewById(R.id.fy_ar_lg).setVisibility(View.GONE);
                findViewById(R.id.fy_ar_p).setVisibility(View.VISIBLE);
                break;
            case R.id.btn_direction_down:
                lGradientArcHeaderView.setDirection(BaseArcHeaderView.DIRECTION_DOWN_OUT_SIDE);
                pictureArcHeaderView.setDirection(BaseArcHeaderView.DIRECTION_DOWN_OUT_SIDE);
                break;
            case R.id.btn_direction_up:
                lGradientArcHeaderView.setDirection(BaseArcHeaderView.DIRECTION_DOWN_IN_SIDE);
                pictureArcHeaderView.setDirection(BaseArcHeaderView.DIRECTION_DOWN_IN_SIDE);
                break;
        }
    }
}
