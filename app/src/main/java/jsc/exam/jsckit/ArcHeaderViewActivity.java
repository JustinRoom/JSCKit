package jsc.exam.jsckit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.SeekBar;

import jsc.kit.archeaderview.BaseArcHeaderView;
import jsc.kit.archeaderview.LGradientArcHeaderView;
import jsc.kit.archeaderview.PictureArcHeaderView;

public class ArcHeaderViewActivity extends AppCompatActivity {

    final String TAG = getClass().getSimpleName();
    LGradientArcHeaderView lGradientArcHeaderView;
    PictureArcHeaderView pictureArcHeaderView;
    SeekBar seekBar;
    int maxArcHeight = 200;
    int type = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_header_view);

        lGradientArcHeaderView = findViewById(R.id.lg_header_view);
        pictureArcHeaderView = findViewById(R.id.p_header_view);
        seekBar = findViewById(R.id.seek_bar);

        pictureArcHeaderView.setVisibility(View.GONE);
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
                lGradientArcHeaderView.setVisibility(View.VISIBLE);
                pictureArcHeaderView.setVisibility(View.GONE);
                break;
            case R.id.btn_type_pic:
                if (type == 1)
                    return;
                type = 1;
                lGradientArcHeaderView.setVisibility(View.GONE);
                pictureArcHeaderView.setVisibility(View.VISIBLE);
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
