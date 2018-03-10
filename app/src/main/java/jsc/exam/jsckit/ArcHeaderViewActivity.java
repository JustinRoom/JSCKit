package jsc.exam.jsckit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import jsc.kit.archeaderview.BaseArcHeaderView;
import jsc.kit.archeaderview.LGradientArcHeaderView;
import jsc.kit.archeaderview.PictureArcHeaderView;

public class ArcHeaderViewActivity extends AppCompatActivity {

    LGradientArcHeaderView lGradientArcHeaderView;
    PictureArcHeaderView pictureArcHeaderView;
    int index = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_header_view);

        lGradientArcHeaderView = findViewById(R.id.lg_header_view);
        pictureArcHeaderView = findViewById(R.id.p_header_view);

        pictureArcHeaderView.setVisibility(View.GONE);
    }

    public void widgetClick(View view) {
        index++;
        if (index > 3)
            index = 0;
        switch (index) {
            case 0:
                lGradientArcHeaderView.setVisibility(View.VISIBLE);
                pictureArcHeaderView.setVisibility(View.GONE);
                lGradientArcHeaderView.setDirection(BaseArcHeaderView.DIRECTION_DOWN_OUT_SIDE);
                break;
            case 1:
                lGradientArcHeaderView.setVisibility(View.VISIBLE);
                pictureArcHeaderView.setVisibility(View.GONE);
                lGradientArcHeaderView.setDirection(BaseArcHeaderView.DIRECTION_DOWN_IN_SIDE);
                break;
            case 2:
                lGradientArcHeaderView.setVisibility(View.GONE);
                pictureArcHeaderView.setVisibility(View.VISIBLE);
                pictureArcHeaderView.setDirection(BaseArcHeaderView.DIRECTION_DOWN_OUT_SIDE);
                break;
            case 3:
                lGradientArcHeaderView.setVisibility(View.GONE);
                pictureArcHeaderView.setVisibility(View.VISIBLE);
                pictureArcHeaderView.setDirection(BaseArcHeaderView.DIRECTION_DOWN_IN_SIDE);
                break;
        }
    }
}
